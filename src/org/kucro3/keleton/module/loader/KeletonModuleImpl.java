package org.kucro3.keleton.module.loader;

import org.kucro3.keleton.module.KeletonInstance;
import org.kucro3.keleton.module.KeletonModule;
import org.kucro3.keleton.module.Module;
import org.kucro3.keleton.module.event.KeletonModuleEvent;
import org.kucro3.keleton.module.exception.KeletonModuleBadDependencyException;
import org.kucro3.keleton.module.exception.KeletonModuleException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class KeletonModuleImpl implements KeletonModule {
    KeletonModuleImpl(KeletonInstance instance, Module info)
    {
        this.instance = instance;
        this.info = info;
        this.state = State.MOUNTED;
    }

    @Override
    public String getId()
    {
        return this.info.id();
    }

    @Override
    public Set<String> getDependencies()
    {
        return new HashSet<>(Arrays.asList(info.dependencies()));
    }

    @Override
    public KeletonInstance getInstance()
    {
        return instance;
    }

    @Override
    public boolean supportDisabling()
    {
        return info.supportDisabling();
    }

    @Override
    public State getState()
    {
        return state;
    }

    void fence()
    {
        synchronized(state) {
            this.state = State.FENCED;
        }
    }

    synchronized boolean enterFence()
    {
        synchronized(state) {
            if (state.equals(State.FENCED))
                return false;

            if (fencedState != null)
                throw new IllegalStateException();

            fencedState = state;
            state = State.FENCED;
            return true;
        }
    }

    synchronized void exitFence()
    {
        if(fencedState == null)
            throw new IllegalStateException();
        state = fencedState;
        fencedState = null;
    }

    Void tryRecovery(Throwable exception, State expected)
    {
        return tryRecovery(exception, expected, this::postExcepted);
    }

    Void tryRecovery(Throwable e, State expected, BiConsumer<State, Throwable> poster)
    {
        try {
            if(!prepostRecovery(e, expected)) {
                this.state = State.FAILED;
                return null;
            }
            if(!this.instance.tryRecovery(this, expected, e)) {
                this.state = State.FAILED;
                poster.accept(expected, e);
            }
            else {
                this.state = expected;
                postRecovered(e, expected, expected);
            }
        } catch (Throwable recoveryException) {
            this.state = State.FAILED;
            postFailedOnRecovery(e, expected, recoveryException);
        }
        return null;
    }

    void releaseDependencies()
    {
        for(String dep : seq.getDependencies(getId()))
            seq.getModule(dep).exitFence();
    }

    void transformed(State to)
    {
        this.state = to;
        postTransformed(to);
    }

    @Override
    public void escapeState(State state)
    {
        while(this.state.equals(state));
    }

    @Override
    public boolean waitForDependencies()
    {
        return queueDependencies(true);
    }

    boolean queueDependencies(boolean wait)
    {
        if(!isBound())
            return false;

        Set<String> dependencies = seq.getDependencies(getId());
        if(!wait)
        {
            ArrayList<KeletonModuleImpl> queued = new ArrayList<>();
            for (String dep : dependencies) {
                KeletonModuleImpl module = seq.getModule(dep);
                if (module.getState().equals(State.FENCED)) {
                    for(KeletonModuleImpl impl : queued)
                        impl.exitFence();
                    return false;
                }
                else {
                    if(!module.enterFence())
                    {
                        for(KeletonModuleImpl impl : queued)
                            impl.exitFence();
                        return false;
                    }
                    queued.add(module);
                }
            }
        }
        else
            for (String dep : dependencies) {
                KeletonModuleImpl module = seq.getModule(dep);
                if (module.getState().equals(State.FENCED))
                    while(!module.enterFence())
                        module.escapeFence();
            }

        return true;
    }

    private Optional<CompletableFuture<Void>> transform(State to, ActionFunction function, Supplier<Boolean> checker, boolean wait)
    {
        if(!checkBind() || !checker.get() || !checkConvert(to) || !prepostTransformation(to))
            return Optional.empty();

        fence();

        try {
            if(!queueDependencies(wait))
                return Optional.empty();

            KeletonModule module;
            for(String dep : seq.getDependencies(getId()))
                if((module = seq.getModule(dep)).getState().equals(State.FAILED))
                {
                    final KeletonModule currentModule = module;
                    tryRecovery(new KeletonModuleBadDependencyException(currentModule), to, (s, e) -> postBadDependency(s, currentModule));

                    if(this.state.equals(State.FAILED))
                        return Optional.empty();
                }

            CompletableFuture<Void> future = function.get();

            future.exceptionally((e) -> tryRecovery(e, to));

            return Optional.of(future
                            .thenAccept((unused) -> transformed(to))
                            .thenAccept((unused) -> releaseDependencies()));
        } catch (Exception e) {
            tryRecovery(e, to);
        }

        return Optional.empty();
    }

    @Override
    public Optional<CompletableFuture<Void>> load()
    {
        return load0(true);
    }

    @Override
    public Optional<CompletableFuture<Void>> loadImmediately()
    {
        return load0(false);
    }

    private Optional<CompletableFuture<Void>> load0(boolean wait)
    {
        return transform(State.LOADED, () -> instance.onLoad(), () -> true, wait);
    }

    @Override
    public Optional<CompletableFuture<Void>> enable()
    {
        return enable0(true);
    }

    @Override
    public Optional<CompletableFuture<Void>> enableImmediately()
    {
        return enable0(false);
    }

    private Optional<CompletableFuture<Void>> enable0(boolean wait)
    {
        return transform(State.ENABLED, () -> instance.onEnable(), () -> true, wait);
    }

    @Override
    public Optional<CompletableFuture<Void>> disable()
    {
        return disable0(true);
    }

    @Override
    public Optional<CompletableFuture<Void>> disableImmediately()
    {
        return disable0(false);
    }

    private Optional<CompletableFuture<Void>> disable0(boolean wait)
    {
        return transform(State.DISABLED, () -> instance.onDisable(), () -> checkDisablingFunction(), wait);
    }

    @Override
    public Optional<CompletableFuture<Void>> destroy()
    {
        return destroy0(true);
    }

    @Override
    public Optional<CompletableFuture<Void>> destroyImmediately()
    {
        return destroy0(false);
    }

    private Optional<CompletableFuture<Void>> destroy0(boolean wait)
    {
        return transform(State.DESTROYED, () -> instance.onDestroy(), () -> true, wait);
    }

    void postBadDependency(State expected, KeletonModule bad)
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.BadDependency(this, this.state, expected, createCause(), bad));
    }

    void postExcepted(State expected, Throwable e)
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.Failed(this, this.state, expected, createCause(), e));
    }

    void postTransformed(State to)
    {
        Sponge.getEventManager().post(new StateTransformationEventImpl.Transformed(this, this.state, to, createCause()));
    }

    void postFailedOnRecovery(Throwable source, State expected, Throwable exception)
    {
        Sponge.getEventManager().post(new RecoveryEventImpl.Failed(this, expected, source, createCause(), exception));
    }

    void postRecovered(Throwable source, State expected, State achieved)
    {
        Sponge.getEventManager().post(new RecoveryEventImpl.Recovered(this, expected, source, createCause(), achieved));
    }

    boolean prepostRecovery(Throwable source, State expected)
    {
        KeletonModuleEvent.Recovery.Pre event = new RecoveryEventImpl.Pre(this, expected, source, createCause());
        Sponge.getEventManager().post(event);

        if(event.isCancelled())
        {
            Cause cause = createCause();
            if(event.isCancelledWithCause())
                cause = cause.merge(event.getCancellationCause().get());

            Sponge.getEventManager().post(new RecoveryEventImpl.Cancelled(this, expected, source, cause));
            return false;
        }

        return true;
    }

    boolean prepostTransformation(State expected)
    {
        KeletonModuleEvent.StateTransformation.Pre event = new StateTransformationEventImpl.Pre(this, this.state, expected, createCause());
        Sponge.getEventManager().post(event);

        if(event.isCancelled())
        {
            Cause cause = createCause();
            if(event.isCancelledWithCause())
                cause = cause.merge(event.getCancellationCause().get());

            Sponge.getEventManager().post(new StateTransformationEventImpl.Cancelled(this, this.state, expected, cause));
            return false;
        }

        return true;
    }

    boolean checkDisablingFunction()
    {
        if(!supportDisabling())
        {
            String msg = "Disabing operation not supported";
            Sponge.getEventManager().post(new StateTransformationEventImpl.Ignored(this, this.state, State.DISABLED, createCause(), msg));
            return false;
        }
        return true;
    }

    boolean touchState(State state)
    {
        switch(this.state)
        {
            case FENCED:
                return false;

            case MOUNTED:
            case DESTROYED:
            case FAILED:
                switch(state)
                {
                    case LOADED:
                        return true;

                    default:
                        return false;
                }

            case LOADED:
            case DISABLED:
                switch(state)
                {
                    case ENABLED:
                    case DESTROYED:
                        return true;

                    default:
                        return false;
                }

            case ENABLED:
                switch(state)
                {
                    case DISABLED:
                        return true;

                    default:
                        return false;
                }
        }

        return true;
    }

    boolean checkConvert(State state)
    {
        if(!touchState(state))
            stateFailure(state);
        else
            return true;
        return false;
    }

    void stateFailure(State state)
    {
        String msg = "Cannot convert the current state " + this.state.name() + " to " + state.name();
        Sponge.getEventManager().post(new StateTransformationEventImpl.Ignored(this, this.state, state, createCause(), msg));
    }

    Cause createCause()
    {
        return Cause.source(this).build();
    }

    boolean isBound()
    {
        return this.seq != null;
    }

    boolean checkBind()
    {
        boolean result = isBound();
        if(!result)
            Sponge.getEventManager().post(new StateTransformationEventImpl.Ignored(this, this.state, state, createCause(), "Not binded"));
        return result;
    }

    synchronized void bind(ModuleSequence seq) throws KeletonModuleException
    {
        if(this.seq != null)
            throw new KeletonModuleException("Already binded: " + this.getId());
        this.seq = seq;
    }

    DisablingCallback callback;

    ModuleSequence seq;

    volatile State fencedState;

    private volatile State state;

    private final KeletonInstance instance;

    private final Module info;

    static interface DisablingCallback
    {
        void onDisable(KeletonModuleImpl module) throws KeletonModuleException;
    }

    static interface ActionFunction
    {
        CompletableFuture<Void> get() throws Exception;
    }
}
