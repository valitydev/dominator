package com.empayre.dominator.dao;

import com.empayre.dominator.config.PostgresqlSpringBootITest;
import com.empayre.dominator.dao.dominant.iface.DominantDao;
import com.empayre.dominator.dao.dominant.impl.ProviderDaoImpl;
import com.empayre.dominator.dao.dominant.impl.TerminalDaoImpl;
import com.empayre.dominator.dao.party.iface.*;
import com.empayre.dominator.dao.party.impl.TermSetHierarchyDaoImpl;
import com.empayre.dominator.domain.tables.pojos.*;
import com.empayre.dominator.utils.HashUtil;
import dev.vality.testcontainers.annotations.util.RandomBeans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PostgresqlSpringBootITest
public class DaoTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProviderDaoImpl providerDao;

    @Autowired
    private TermSetHierarchyDaoImpl termSetHierarchyDao;

    @Autowired
    private TerminalDaoImpl terminalDao;

    @Autowired
    private DominantDao dominantDao;

    @Autowired
    private ContractAdjustmentDao contractAdjustmentDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractorDao contractorDao;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    private PayoutToolDao payoutToolDao;

    @Autowired
    private ShopDao shopDao;

    @Test
    public void dominantDaoTest() {
        Provider provider = RandomBeans.random(Provider.class);
        provider.setTerminalObject(new byte[0]);
        provider.setWalletTermsObject(new byte[0]);
        provider.setPaymentTermsObject(new byte[0]);
        provider.setRecurrentPaytoolTermsObject(new byte[0]);
        provider.setParamsSchemaObject(new byte[0][0]);
        provider.setCurrent(true);
        providerDao.save(provider);
        providerDao.updateNotCurrent(provider.getProviderRefId());

        TermSetHierarchy termSetHierarchy = RandomBeans.random(TermSetHierarchy.class);
        termSetHierarchy.setCurrent(true);
        termSetHierarchyDao.save(termSetHierarchy);
        termSetHierarchyDao.updateNotCurrent(termSetHierarchy.getTermSetHierarchyRefId());

        Terminal terminal = RandomBeans.random(Terminal.class);
        terminal.setCurrent(true);
        terminalDao.save(terminal);
        terminalDao.updateNotCurrent(terminal.getTerminalRefId());

        OptionalLong maxVersionId = LongStream.of(
                provider.getVersionId(),
                termSetHierarchy.getVersionId()).max();

        dominantDao.updateLastVersionId(maxVersionId.getAsLong());
        Long lastVersionId = dominantDao.getLastVersionId();

        assertEquals(maxVersionId.getAsLong(), lastVersionId.longValue());
    }

    @Test
    public void contractAdjustmentDaoTest() {
        Contract contract = RandomBeans.random(Contract.class);
        contract.setCurrent(true);
        Long cntrctId = contractDao.save(contract).get();
        List<ContractAdjustment> contractAdjustments = RandomBeans.randomListOf(10, ContractAdjustment.class);
        contractAdjustments.forEach(contractAdjustment -> contractAdjustment.setContractId(cntrctId));
        contractAdjustmentDao.save(contractAdjustments);
        List<ContractAdjustment> byCntrctId = contractAdjustmentDao.getByContractId(cntrctId);
        assertEquals(new HashSet(contractAdjustments), new HashSet(byCntrctId));
    }

    @Test
    public void contractDaoTest() {
        Contract contract = RandomBeans.random(Contract.class);
        contract.setCurrent(true);
        contractDao.save(contract);
        Contract contractGet = contractDao.get(contract.getPartyId(), contract.getContractId());
        assertEquals(contract, contractGet);
    }

    @Test
    public void contractorDaoTest() {
        Contractor contractor = RandomBeans.random(Contractor.class);
        contractor.setCurrent(true);
        contractorDao.save(contractor);
        Contractor contractorGet = contractorDao.get(contractor.getPartyId(), contractor.getContractorId());
        assertEquals(contractor, contractorGet);
        Integer changeId = contractor.getChangeId() + 1;
        contractor.setChangeId(changeId);
        Long oldId = contractor.getId();
        contractor.setId(contractor.getId() + 1);
        contractorDao.save(contractor);
        contractorDao.updateNotCurrent(oldId);
    }

    @Test
    public void partyDaoTest() {
        Party party = RandomBeans.random(Party.class);
        party.setCurrent(true);
        partyDao.save(party);
        Party partyGet = partyDao.get(party.getPartyId());
        assertEquals(party, partyGet);
        Long oldId = party.getId();

        Integer changeId = party.getChangeId() + 1;
        party.setChangeId(changeId);
        party.setId(party.getId() + 1);
        partyDao.save(party);
        partyDao.updateNotCurrent(oldId);

        partyGet = partyDao.get(party.getPartyId());
        assertEquals(changeId, partyGet.getChangeId());
    }

    @Test
    public void payoutToolDaoTest() {
        Contract contract = RandomBeans.random(Contract.class);
        contract.setCurrent(true);
        Long contractId = contractDao.save(contract).get();
        List<PayoutTool> payoutTools = RandomBeans.randomListOf(10, PayoutTool.class);
        payoutTools.forEach(pt -> pt.setContractId(contractId));
        payoutToolDao.save(payoutTools);
        List<PayoutTool> byCntrctId = payoutToolDao.getByContractId(contractId);
        assertEquals(new HashSet(payoutTools), new HashSet(byCntrctId));
    }

    @Test
    public void shopDaoTest() {
        Shop shop = RandomBeans.random(Shop.class);
        shop.setCurrent(true);
        shopDao.save(shop);
        Shop shopGet = shopDao.get(shop.getPartyId(), shop.getShopId());
        assertEquals(shop, shopGet);

        Integer changeId = shop.getChangeId() + 1;
        shop.setChangeId(changeId);
        Long id = shop.getId();
        shop.setId(id + 1);
        shopDao.save(shop);
        shopDao.updateNotCurrent(id);
    }

    @Test
    public void getIntHashTest() {
        Integer javaHash = HashUtil.getIntHash("kek");
        Integer postgresHash =
                jdbcTemplate.queryForObject("select ('x0'||substr(md5('kek'), 1, 7))::bit(32)::int", Integer.class);
        assertEquals(javaHash, postgresHash);
    }
}
