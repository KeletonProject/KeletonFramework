package org.kucro3.keleton.impl.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kucro3.keleton.config.ConfigurationOperator;
import org.kucro3.keleton.config.ConfigurationService;
import org.kucro3.klink.Klink;

class ConfigurationServiceImpl implements ConfigurationService {
	ConfigurationServiceImpl()
	{
		this.operatorKlink = new ConfigurationOperatorImpl.ForKlink(new Klink());
		this.operators = new HashMap<>();
		
		this.operators.put("KLINK", this.operatorKlink);
	}
	
	@Override
	public Optional<ConfigurationOperator> getOperator(String name)
	{
		return Optional.ofNullable(operators.get(name));
	}
	
	final ConfigurationOperatorImpl.ForKlink operatorKlink;
	
	private final Map<String, ConfigurationOperator> operators;
}
