package com.example.aot;

import com.azure.spring.cloud.service.implementation.passwordless.AzurePasswordlessProperties;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.context.properties.bind.BindableRuntimeHintsRegistrar;

public class AotHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        BindableRuntimeHintsRegistrar.forTypes(AzurePasswordlessProperties.class)
                .registerHints(hints);
        hints.reflection().registerTypeIfPresent(classLoader,
                "com.azure.identity.providers.postgresql.AzureIdentityPostgresqlAuthenticationPlugin",
                MemberCategory.DECLARED_CLASSES,
                MemberCategory.DECLARED_FIELDS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_METHODS);
        hints.reflection().registerTypeIfPresent(classLoader,
                "com.azure.spring.cloud.service.implementation.identity.credential.provider.SpringTokenCredentialProvider",
                MemberCategory.DECLARED_CLASSES,
                MemberCategory.DECLARED_FIELDS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_METHODS);
    }

}
