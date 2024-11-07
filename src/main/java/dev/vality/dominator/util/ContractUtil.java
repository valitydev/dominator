package dev.vality.dominator.util;

import dev.vality.damsel.domain.*;
import dev.vality.geck.common.util.TypeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContractUtil {
    public static List<dev.vality.dominator.domain.tables.pojos.ContractAdjustment> convertContractAdjustments(
            Contract contract,
            long cntrctId) {
        return contract.getAdjustments().stream()
                .map(ca -> convertContractAdjustment(ca, cntrctId))
                .collect(Collectors.toList());
    }

    public static dev.vality.dominator.domain.tables.pojos.ContractAdjustment convertContractAdjustment(
            ContractAdjustment contractAdjustment,
            long contractId
    ) {
        var adjustment = new dev.vality.dominator.domain.tables.pojos.ContractAdjustment();
        adjustment.setContractId(contractId);
        adjustment.setContractAdjustmentId(contractAdjustment.getId());
        adjustment.setCreatedAt(TypeUtil.stringToLocalDateTime(contractAdjustment.getCreatedAt()));
        if (contractAdjustment.isSetValidSince()) {
            adjustment.setValidSince(TypeUtil.stringToLocalDateTime(contractAdjustment.getValidSince()));
        }
        if (contractAdjustment.isSetValidUntil()) {
            adjustment.setValidUntil(TypeUtil.stringToLocalDateTime(contractAdjustment.getValidUntil()));
        }
        adjustment.setTermsId(contractAdjustment.getTerms().getId());
        return adjustment;
    }

    public static List<dev.vality.dominator.domain.tables.pojos.PayoutTool> convertPayoutTools(
            Contract contract,
            long contractId
    ) {
        return contract.getPayoutTools().stream().map(pt -> convertPayoutTool(pt, contractId))
                .collect(Collectors.toList());
    }

    public static dev.vality.dominator.domain.tables.pojos.PayoutTool buildPayoutTool(
            long contractId,
            String payoutToolId,
            LocalDateTime createdAt,
            String currCode,
            PayoutToolInfo payoutToolInfo
    ) {
        var payoutTool = new dev.vality.dominator.domain.tables.pojos.PayoutTool();
        payoutTool.setContractId(contractId);
        payoutTool.setPayoutToolId(payoutToolId);
        payoutTool.setCreatedAt(createdAt);
        payoutTool.setCurrencyCode(currCode);
        setPayoutToolInfo(payoutTool, payoutToolInfo);
        return payoutTool;
    }

    public static dev.vality.dominator.domain.tables.pojos.PayoutTool convertPayoutTool(
            PayoutTool pt,
            long cntrctId
    ) {
        return buildPayoutTool(cntrctId, pt.getId(), TypeUtil.stringToLocalDateTime(pt.getCreatedAt()),
                pt.getCurrency().getSymbolicCode(), pt.getPayoutToolInfo());
    }

    public static void setPayoutToolInfo(dev.vality.dominator.domain.tables.pojos.PayoutTool payoutTool,
                                         PayoutToolInfo payoutToolInfoSource) {
        var payoutToolInfo = TypeUtil.toEnumField(
                payoutToolInfoSource.getSetField().getFieldName(),
                dev.vality.dominator.domain.enums.PayoutToolInfo.class
        );
        if (payoutToolInfo == null) {
            throw new IllegalArgumentException("Illegal payout tool info: " + payoutToolInfoSource);
        }
        payoutTool.setPayoutToolInfo(payoutToolInfo);
        if (payoutToolInfoSource.isSetRussianBankAccount()) {
            RussianBankAccount russianBankAccount = payoutToolInfoSource.getRussianBankAccount();
            payoutTool.setPayoutToolInfoRussianBankAccount(russianBankAccount.getAccount());
            payoutTool.setPayoutToolInfoRussianBankName(russianBankAccount.getBankName());
            payoutTool.setPayoutToolInfoRussianBankPostAccount(russianBankAccount.getBankPostAccount());
            payoutTool.setPayoutToolInfoRussianBankBik(russianBankAccount.getBankBik());
        } else if (payoutToolInfoSource.isSetInternationalBankAccount()) {
            InternationalBankAccount internationalBankAccount = payoutToolInfoSource.getInternationalBankAccount();
            payoutTool.setPayoutToolInfoInternationalBankNumber(internationalBankAccount.getNumber());
            payoutTool.setPayoutToolInfoInternationalBankAccountHolder(internationalBankAccount.getAccountHolder());
            payoutTool.setPayoutToolInfoInternationalBankIban(internationalBankAccount.getIban());

            if (internationalBankAccount.isSetBank()) {
                InternationalBankDetails bankDetails = internationalBankAccount.getBank();
                payoutTool.setPayoutToolInfoInternationalBankName(bankDetails.getName());
                payoutTool.setPayoutToolInfoInternationalBankAddress(bankDetails.getAddress());
                payoutTool.setPayoutToolInfoInternationalBankBic(bankDetails.getBic());
                payoutTool.setPayoutToolInfoInternationalBankAbaRtn(bankDetails.getAbaRtn());
                payoutTool.setPayoutToolInfoInternationalBankCountryCode(
                        Optional.ofNullable(bankDetails.getCountry())
                                .map(country -> country.toString())
                                .orElse(null)
                );
            }
            if (internationalBankAccount.isSetCorrespondentAccount()) {
                InternationalBankAccount correspondentBankAccount = internationalBankAccount.getCorrespondentAccount();
                payoutTool.setPayoutToolInfoInternationalCorrespondentBankNumber(correspondentBankAccount.getNumber());
                payoutTool.setPayoutToolInfoInternationalCorrespondentBankAccount(
                        correspondentBankAccount.getAccountHolder());
                payoutTool.setPayoutToolInfoInternationalCorrespondentBankIban(correspondentBankAccount.getIban());

                if (correspondentBankAccount.isSetBank()) {
                    InternationalBankDetails correspondentBankDetails = correspondentBankAccount.getBank();
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankName(correspondentBankDetails.getName());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankAddress(
                            correspondentBankDetails.getAddress());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankBic(correspondentBankDetails.getBic());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankAbaRtn(
                            correspondentBankDetails.getAbaRtn());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankCountryCode(
                            Optional.ofNullable(correspondentBankDetails.getCountry())
                                    .map(country -> country.toString())
                                    .orElse(null)
                    );
                }
            }
        } else if (payoutToolInfoSource.isSetWalletInfo()) {
            payoutTool.setPayoutToolInfoWalletInfoWalletId(payoutToolInfoSource.getWalletInfo().getWalletId());
        }
    }

    public static void fillContractLegalAgreementFields(
            dev.vality.dominator.domain.tables.pojos.Contract contract,
            LegalAgreement legalAgreement
    ) {
        contract.setLegalAgreementId(legalAgreement.getLegalAgreementId());
        contract.setLegalAgreementSignedAt(TypeUtil.stringToLocalDateTime(legalAgreement.getSignedAt()));
        if (legalAgreement.isSetValidUntil()) {
            contract.setLegalAgreementValidUntil(TypeUtil.stringToLocalDateTime(legalAgreement.getValidUntil()));
        }
    }

    public static void fillReportPreferences(dev.vality.dominator.domain.tables.pojos.Contract contract,
                                             ServiceAcceptanceActPreferences serviceAcceptanceActPreferences) {
        contract.setReportActScheduleId(serviceAcceptanceActPreferences.getSchedule().getId());
        contract.setReportActSignerPosition(serviceAcceptanceActPreferences.getSigner().getPosition());
        contract.setReportActSignerFullName(serviceAcceptanceActPreferences.getSigner().getFullName());
        RepresentativeDocument representativeDocument =
                serviceAcceptanceActPreferences.getSigner().getDocument();
        var reportActSignerDocument = TypeUtil.toEnumField(
                        representativeDocument.getSetField().getFieldName(),
                        dev.vality.dominator.domain.enums.RepresentativeDocument.class
                );
        if (reportActSignerDocument == null) {
            throw new IllegalArgumentException("Illegal representative document: " + representativeDocument);
        }
        contract.setReportActSignerDocument(reportActSignerDocument);
        if (representativeDocument.isSetPowerOfAttorney()) {
            contract.setReportActSignerDocPowerOfAttorneyLegalAgreementId(
                    representativeDocument.getPowerOfAttorney().getLegalAgreementId());
            contract.setReportActSignerDocPowerOfAttorneySignedAt(
                    TypeUtil.stringToLocalDateTime(representativeDocument.getPowerOfAttorney().getSignedAt()));
            if (representativeDocument.getPowerOfAttorney().isSetValidUntil()) {
                contract.setReportActSignerDocPowerOfAttorneyValidUntil(
                        TypeUtil.stringToLocalDateTime(representativeDocument.getPowerOfAttorney().getValidUntil()));
            }
        }
    }

    public static void setNullReportPreferences(dev.vality.dominator.domain.tables.pojos.Contract contract) {
        contract.setReportActScheduleId(null);
        contract.setReportActSignerPosition(null);
        contract.setReportActSignerFullName(null);
        contract.setReportActSignerDocument(null);
        contract.setReportActSignerDocPowerOfAttorneyLegalAgreementId(null);
        contract.setReportActSignerDocPowerOfAttorneySignedAt(null);
        contract.setReportActSignerDocPowerOfAttorneyValidUntil(null);
    }

}
