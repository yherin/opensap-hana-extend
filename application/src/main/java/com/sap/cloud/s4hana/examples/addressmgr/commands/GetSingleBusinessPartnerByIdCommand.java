package com.sap.cloud.s4hana.examples.addressmgr.commands;

import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import org.slf4j.Logger;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;

public class GetSingleBusinessPartnerByIdCommand {
    private static final Logger logger = CloudLoggerFactory.getLogger(GetSingleBusinessPartnerByIdCommand.class);

    private final BusinessPartnerService service;
    private final String id;

    public GetSingleBusinessPartnerByIdCommand(final BusinessPartnerService service, final String id) {
        this.service = service;
        this.id = id;
    }

    public BusinessPartner execute() throws Exception {
        // TODO: Replace with Virtual Data Model query
       return this.service.getBusinessPartnerByKey(id).select(
               BusinessPartner.BUSINESS_PARTNER,
               BusinessPartner.LAST_NAME,
               BusinessPartner.FIRST_NAME,
               BusinessPartner.IS_MALE,
               BusinessPartner.IS_FEMALE,
               BusinessPartner.CREATION_DATE,
               BusinessPartner.TO_BUSINESS_PARTNER_ADDRESS.select(
                       BusinessPartnerAddress.ALL_FIELDS
               )
               ).execute();
    }
}
