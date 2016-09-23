package net.tmcf.graylog.plugins;

import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;

import com.google.inject.Scopes;

import java.util.Collections;
import java.util.Set;

public class BlacklistModule extends PluginModule {
    
	@Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    @Override
    protected void configure() {
        
    	/*
         * Register plugin types
         */
    	
    	bind(BlacklistPluginService.class).to(BlacklistPluginServiceImpl.class).in(Scopes.SINGLETON);
    	
    	addMessageFilter(Blacklist.class);
    	addRestResource(BlacklistPluginResource.class);
    	
    	addConfigBeans();
    	
    }
    
}
