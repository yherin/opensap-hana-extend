package com.sap.cloud.s4hana.examples.addressmgr.commands;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sap.cloud.sdk.cloudplatform.cache.CacheKey;
import com.sap.cloud.sdk.s4hana.connectivity.CachingErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.Order;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;

import javax.annotation.Nonnull;

public class GetAllBusinessPartnersCommand extends CachingErpCommand<List<BusinessPartner>> {
    private static final Logger logger = CloudLoggerFactory.getLogger(GetAllBusinessPartnersCommand.class);

    private static final String CATEGORY_PERSON = "1";

    private static final Cache<CacheKey, List<BusinessPartner>> cache =
            CacheBuilder.newBuilder()
                    .maximumSize(50)
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .build();

    private final BusinessPartnerService service;

    public GetAllBusinessPartnersCommand(final BusinessPartnerService service) {
        super(GetAllBusinessPartnersCommand.class);
        this.service = service;
    }

    @Nonnull
    @Override
    protected Cache<CacheKey, List<BusinessPartner>> getCache() {
        return cache;
    }


    @Override
    protected List<BusinessPartner> getFallback() {
        logger.warn("Falling back ", getExecutionException());
        return new ArrayList<>();
    }

    @Override
    @Nonnull
    protected List<BusinessPartner> runCacheable() throws Exception {
        // TODO: Replace with Virtual Data Model query
        return this.service.getAllBusinessPartner()
                .select(BusinessPartner.BUSINESS_PARTNER,
                        BusinessPartner.FIRST_NAME,
                        BusinessPartner.LAST_NAME)
                .filter(BusinessPartner.BUSINESS_PARTNER_CATEGORY.eq(CATEGORY_PERSON))
                .orderBy(BusinessPartner.LAST_NAME, Order.DESC).execute();

    }

}
