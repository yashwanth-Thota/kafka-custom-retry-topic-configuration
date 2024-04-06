package com.yashwanth.kafka;

import org.springframework.kafka.support.Suffixer;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class RetrySuffixer extends Suffixer {

    final String prefix;
    final String suffix;

    final String environment;

    public RetrySuffixer(String prefix,String suffix, String env) {
        super(suffix);
        this.prefix = prefix;
        this.suffix = suffix;
        this.environment = env;
    }
    public String maybeAddTo(String source) {
        return StringUtils.hasText(source)
            ? prefix.concat(String.format("%s-%s" , suffix,environment))
            : source;
    }

    @Override
    public Collection<String> maybeAddTo(Collection<String> sources) {
        return this.maybeAddTo(sources);
    }
}
