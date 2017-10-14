package org.kucro3.keleton.service.ban;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.kucro3.keleton.UniqueService;
import org.kucro3.keleton.ban.EnhancedBanService;
import org.kucro3.keleton.cause.FromUniqueService;
import org.kucro3.keleton.sql.DatabaseConnection;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.Named;
import org.spongepowered.api.event.user.BanUserEvent;
import org.spongepowered.api.event.user.PardonUserEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.Ban.Ip;
import org.spongepowered.api.util.ban.Ban.Profile;
import org.spongepowered.api.util.ban.BanTypes;

@SuppressWarnings("unchecked")
public class BanServiceImpl implements EnhancedBanService, UniqueService {
	BanServiceImpl(DatabaseConnection connection, Logger logger) throws SQLException
	{
		this.db = connection;
		this.uuid = UUID.randomUUID();
		this.ensureTable();
		Sponge.getEventManager().registerListeners(SpongeMain.getInstance(), this);
	}
	
	@Override
	public Optional<? extends Ban> addBan(Ban ban)
	{
		if(ban.getType().equals(BanTypes.IP))
			return Optional.empty();
		if(!supported(ban.getBanCommandSource().orElse(null)))
			return Optional.empty();
		
		Ban.Profile profileBan = (Ban.Profile) ban;
		UUID uuid = profileBan.getProfile().getUniqueId();
		
		try {
			if(!cache.containsKey(uuid))
				if(query(uuid))
					update(profileBan);
				else
					insert(profileBan);
			else
				;
			Optional<? extends Ban> ret = Optional.ofNullable(cache.put(uuid, profileBan));
			
			Cause cause;
			Optional<Player> player = Sponge.getServer().getPlayer(profileBan.getProfile().getUniqueId());
			
			if(!player.isPresent())
				return ret;
			
			if(profileBan.getBanCommandSource().isPresent())
				cause = Cause.builder().notifier(profileBan.getBanCommandSource().get()).build();
			else if(profileBan.getBanSource().isPresent())
				cause = Cause.builder().notifier(profileBan.getBanSource().get()).build();
			else
				cause = Cause.builder().build();
			
			cause.merge(SpongeMain.from(this.uuid));
			
			BanUserEvent event = new BanUserEventImpl(cause, player.get(), profileBan);
			Sponge.getEventManager().post(event);
			
			return ret;
		} catch (SQLException e) {
			SpongeMain.getLogger().error("Failed to update ban", e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Ban.Profile> getBanFor(GameProfile profile) 
	{
		UUID uuid = profile.getUniqueId();
		
		try {
			Ban.Profile ban = cache.get(uuid);
			if(ban == null)
				if(query(uuid))
					ban = cache.get(uuid);
				else
					;
			else
				;
			return Optional.ofNullable(ban);
		} catch (SQLException e) {
			SpongeMain.getLogger().error("Failed to get ban", e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Ip> getBanFor(InetAddress address)
	{
		return Optional.empty();
	}

	@Override
	public Collection<? extends Ban> getBans()
	{
		try {
			this.queryAll();
		} catch (SQLException e) {
			// unused
		}
		return Collections.unmodifiableCollection(cache.values());
	}

	@Override
	public Collection<Ip> getIpBans()
	{
		return Collections.emptySet();
	}

	@Override
	public Collection<Profile> getProfileBans() 
	{
		return (Collection<Ban.Profile>) getBans();
	}

	@Override
	public boolean hasBan(Ban ban)
	{
		if(ban.getType().equals(BanTypes.PROFILE))
			return banned(((Ban.Profile) ban).getProfile().getUniqueId());
		return false;
	}

	@Override
	public boolean isBanned(GameProfile profile) 
	{
		return banned(profile.getUniqueId());
	}

	@Override
	public boolean isBanned(InetAddress address)
	{
		return false;
	}

	@Override
	public boolean pardon(GameProfile profile) 
	{
		return pardon(profile.getUniqueId());
	}

	@Override
	public boolean pardon(InetAddress address) 
	{
		return false;
	}

	@Override
	public boolean removeBan(Ban _ban) 
	{
		if(!_ban.getType().equals(BanTypes.PROFILE))
			return false;
		
		Ban.Profile ban = (Ban.Profile) _ban;
		UUID uuid = ban.getProfile().getUniqueId();
		
		return pardon(uuid);
	}
	
	public boolean pardon(UUID uuid)
	{
		try {
			if(!cache.containsKey(uuid))
			{
				query(uuid);
				if(!cache.containsKey(uuid))
					return false;
			}
			delete(uuid);
			Ban.Profile profileBan = cache.remove(uuid);
			
			Cause cause;
			Optional<Player> player = Sponge.getServer().getPlayer(profileBan.getProfile().getUniqueId());
			
			if(!player.isPresent())
				return true;
			
			if(profileBan.getBanCommandSource().isPresent())
				cause = Cause.builder().notifier(profileBan.getBanCommandSource().get()).build();
			else if(profileBan.getBanSource().isPresent())
				cause = Cause.builder().notifier(profileBan.getBanSource().get()).build();
			else
				cause = Cause.builder().build();
			
			cause.merge(SpongeMain.from(this.uuid));
			
			PardonUserEvent event = new PardonUserEventImpl(cause, player.get(), profileBan);
			Sponge.getEventManager().post(event);
			
			return true;
		} catch (SQLException e) {
			SpongeMain.getLogger().error("Failed to remove ban", e);
			return false;
		}
	}
	
	boolean banned(UUID uuid)
	{
		if(cache.containsKey(uuid))
			return true;
		try {
			query(uuid);
		} catch (SQLException e) {
			SpongeMain.getLogger().error("Failed to query state", e);
			return false;
		}
		return cache.containsKey(uuid);
	}
	
	synchronized void ensureTable() throws SQLException
	{
		db.execute("CREATE TABLE IF NOT EXISTS keleton_banlist "
				 + "("
				 + "UID varchar(255) NOT NULL UNIQUE,"
				 + "TIMESTAMP bigint NOT NULL,"
				 + "EXPIRATION bigint,"
				 + "COMMANDSOURCE varchar(255),"
				 + "SOURCE text,"
				 + "REASON ntext"
			 	 + ") DEFAULT CHARSET=UTF8;");
	}
	
	synchronized boolean query(UUID uuid) throws SQLException
	{
		Optional<ResultSet> optional = db.execute("SELECT * FROM keleton_banlist WHERE UID='" + uuid.toString() + "';");
		if(optional.isPresent())
		{
			ResultSet result = optional.get();
			if(result.next())
				return query0(result);
			else
				return false;
		}
		return false;
	}
	
	synchronized void delete(UUID uuid) throws SQLException
	{
		db.execute("DELETE FROM keleton_banlist WHERE UID=\'" + uuid.toString() + "\';");
	}
	
	private void values(PreparedStatement statement, Ban.Profile ban) throws SQLException
	{
		statement.setLong(1, ban.getCreationDate().toEpochMilli());
		
		statement.setLong(2, ban.isIndefinite() ? 0L : ban.getExpirationDate().get().toEpochMilli());
		
		if(ban.getBanCommandSource().isPresent())
			statement.setString(3, toString(ban.getBanCommandSource().get()));
		else
			statement.setNull(3, Types.VARCHAR);
		
		if(ban.getBanSource().isPresent())
			statement.setString(4, TextSerializers.JSON.serialize(ban.getBanSource().get()));
		else
			statement.setNull(4, Types.OTHER, "text");
		
		if(ban.getReason().isPresent())
			statement.setString(5, TextSerializers.JSON.serialize(ban.getReason().get()));
		else
			statement.setNull(5, Types.OTHER, "ntext");
		
		statement.setString(6, ban.getProfile().getUniqueId().toString());
	}
	
	synchronized void update(final Ban.Profile ban) throws SQLException
	{
		db.process((connection) -> {
			values(connection.prepareStatement("UPDATE keleton_banlist "
											 + "SET TIMESTAMP=?,EXPIRATION=?,COMMANDSOURCE=?,SOURCE=?,REASON=? "
											 + "WHERE UID=?"),
					ban);
		});
	}
	
	synchronized void insert(final Ban.Profile ban) throws SQLException
	{
		db.process((connection) -> {
			values(connection.prepareStatement("INSERT INTO keleton_list "
											 + "(TIMESTAMP, EXPIRATION, COMMANDSOURCE, SOURCE, REASON, UID) "
											 + "VALUES (?, ?, ?, ?, ?, ?)"),
					ban);
		});
	}
	
	synchronized void queryAll() throws SQLException
	{
		Optional<ResultSet> optional = db.execute("SELECT * FROM keleton_banlist;");
		if(optional.isPresent())
		{
			ResultSet result = optional.get();
			while(result.next())
				query0(result);
		}
	}
	
	private final boolean query0(ResultSet result) throws SQLException
	{
		UUID uuid = UUID.fromString(result.getString("UID"));
		long timestamp = result.getLong("TIMESTAMP");
		long expiration = result.getLong("EXPIRATION");
		String commandsource = result.getString("COMMANDSOURCE");
		String source = result.getString("SOURCE");
		String reason = result.getString("REASON");
		
		if(expiration != 0 && expiration < System.currentTimeMillis())
		{
			delete(uuid);
			return false;
		}
		else
		{
			Ban.Profile instance = toInstance(uuid, timestamp, expiration, commandsource, source, reason);
			if(instance != null)
			{
				cache.put(uuid, instance);
				return true;
			}
			return false;
		}
	}
	
	static boolean supported(CommandSource source)
	{
		if(source == null)
			return true;
		if(source instanceof Player)
			return true;
		if(source instanceof ConsoleSource)
			return true;
		return false;
	}
	
	static String toString(CommandSource source)
	{
		if(source == null)
			return null;
		if(source instanceof ConsoleSource)
			return "console";
		if(source instanceof Player)
			return "user:" + ((Player) source).getUniqueId().toString();
		throw new IllegalStateException("Should not reach here");
	}
	
	static Ban.Profile toInstance(UUID uuid, long timestamp, long expiration, String commandsource,
			String source, String reason)
	{
		CompletableFuture<GameProfile> profileFuture = Sponge.getServer().getGameProfileManager().get(uuid);
		GameProfile profile;
		CommandSource commandSource;
		Text textSource, textReason;
		
		if(commandsource != null)
		{
			String[] splitted = commandsource.split(":");
			switch(splitted[0])
			{
			case "console":
				commandSource = Sponge.getServer().getConsole();
				break;
				
			case "user":
				commandSource = Sponge.getServer().getPlayer(uuid).orElse(null);
				break;
				
			default:
				throw new IllegalStateException("Invalid COMMANDSOURCE in database: " + commandsource);
			}
		}
		else
			commandSource = null;
		
		textSource = source == null ? null : TextSerializers.JSON.deserialize(source);
		textReason = reason == null ? null : TextSerializers.JSON.deserialize(reason);
		
		try {
			profile = profileFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
		
		if(profile == null)
			return null;
		
		return (Ban.Profile) Ban.builder()
				.type(BanTypes.PROFILE)
				.profile(profile)
				.startDate(Instant.ofEpochMilli(timestamp))
				.expirationDate(expiration == 0 ? null : Instant.ofEpochMilli(expiration))
				.source(commandSource)
				.source(textSource)
				.reason(textReason)
			.build();
	}
	
	boolean isSelfOperation(FromUniqueService handler)
	{
		return handler.getUniqueId().equals(uuid);
	}
	
	@Listener
	public void _SYNC_onBan(BanUserEvent event, @Named("handler") FromUniqueService handler)
	{
		if(isSelfOperation(handler))
			return;
		
		if(!event.getBan().getType().equals(BanTypes.PROFILE))
			return;
		
		UUID uuid = event.getBan().getProfile().getUniqueId();
		cache.put(uuid, event.getBan());
	}
	
	@Listener
	public void _SYNC_onPardon(PardonUserEvent event, @Named("handler") FromUniqueService handler)
	{
		if(isSelfOperation(handler))
			return;
		
		if(!event.getBan().getType().equals(BanTypes.PROFILE))
			return;
		
		UUID uuid = event.getBan().getProfile().getUniqueId();
		cache.remove(uuid);
	}

	@Override
	public UUID getUniqueId()
	{
		return this.uuid;
	}
	
	private final Map<UUID, Ban.Profile> cache = new ConcurrentHashMap<>();
	
	private final DatabaseConnection db;
	
	private final UUID uuid;
	
	static class BanUserEventImpl implements BanUserEvent.TargetPlayer
	{
		BanUserEventImpl(Cause cause, Player target, Ban.Profile ban)
		{
			this.cause = cause;
			this.target = target;
			this.ban = ban;
		}
		
		@Override
		public User getTargetUser()
		{
			return target;
		}

		@Override
		public Cause getCause() 
		{
			return cause;
		}

		@Override
		public Profile getBan()
		{
			return ban;
		}
		
		@Override
		public Player getTargetEntity() 
		{
			return target;
		}
		
		private final Cause cause;
		
		private final Player target;
		
		private final Ban.Profile ban;
	}
	
	static class PardonUserEventImpl implements PardonUserEvent.TargetPlayer
	{
		PardonUserEventImpl(Cause cause, Player target, Ban.Profile ban)
		{
			this.cause = cause;
			this.target = target;
			this.ban = ban;
		}
		
		@Override
		public Player getTargetUser()
		{
			return target;
		}

		@Override
		public Cause getCause() 
		{
			return cause;
		}

		@Override
		public Profile getBan()
		{
			return ban;
		}
		
		@Override
		public Player getTargetEntity() 
		{
			return target;
		}
		
		private final Cause cause;
		
		private final Player target;
		
		private final Ban.Profile ban;
	}
}
