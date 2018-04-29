/*
 * Copyright 2010-2018 Norwegian Agency for Public Management and eGovernment (Difi)
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package no.difi.oxalis.as2.outbound;

import no.difi.oxalis.api.model.TransmissionIdentifier;
import no.difi.oxalis.api.outbound.TransmissionRequest;
import no.difi.oxalis.api.outbound.TransmissionResponse;
import no.difi.oxalis.api.timestamp.Timestamp;
import no.difi.vefa.peppol.common.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link TransmissionResponse} for use in AS2.
 *
 * @author steinar
 * @author thore
 * @author erlend
 */
class As2TransmissionResponse implements TransmissionResponse, Serializable {

    private static final long serialVersionUID = 4288900204693153668L;

    /**
     * Original transmission request is kept to allow easy access to immutable objects part of the request.
     */
    private final TransmissionRequest transmissionRequest;

    private final TransmissionIdentifier transmissionIdentifier;

    private final Digest digest;

    private final Receipt receipt;

    private final List<Receipt> receipts;

    private final Date timestamp;

    public As2TransmissionResponse(TransmissionIdentifier transmissionIdentifier,
                                   TransmissionRequest transmissionRequest, Digest digest,
                                   byte[] nativeEvidenceBytes, Timestamp timestamp, Date date) {
        this.transmissionIdentifier = transmissionIdentifier;
        this.transmissionRequest = transmissionRequest;
        this.digest = digest;
        this.receipt = Receipt.of("message/disposition-notification", nativeEvidenceBytes);
        this.timestamp = date;

        List<Receipt> receipts = new ArrayList<>();
        receipts.add(receipt);
        if (timestamp.getReceipt().isPresent())
            receipts.add(timestamp.getReceipt().get());
        this.receipts = Collections.unmodifiableList(receipts);
    }

    @Override
    public Header getHeader() {
        return transmissionRequest.getHeader();
    }

    public TransmissionIdentifier getTransmissionIdentifier() {
        return transmissionIdentifier;
    }

    @Override
    public List<Receipt> getReceipts() {
        return receipts;
    }

    @Override
    public Endpoint getEndpoint() {
        return transmissionRequest.getEndpoint();
    }

    @Override
    public Receipt primaryReceipt() {
        return receipt;
    }

    @Override
    public Digest getDigest() {
        return digest;
    }

    @Override
    public TransportProtocol getTransportProtocol() {
        return TransportProtocol.AS2;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }
}
