package org.kucro3.keleton.impl.i18n;

import java.util.Optional;

import org.kucro3.keleton.i18n.LocaleProperties;
import org.kucro3.klink.Variables;
import org.kucro3.klink.Variables.Variable;

class LocalePropertiesImpl implements LocaleProperties {
	LocalePropertiesImpl(Variables vars)
	{
		this.vars = vars;
	}
	
	@Override
	public String by(String id) 
	{
		Optional<Variable> var = vars.getVar(id);
		if(!var.isPresent() || var.get().get() == null)
			return "#" + id;
		else
			return var.get().get().toString();
	}

	@Override
	public boolean contains(String id) 
	{
		return vars.hasVar(id);
	}
	
	private final Variables vars;
}