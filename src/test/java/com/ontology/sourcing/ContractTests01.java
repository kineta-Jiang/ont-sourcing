package com.ontology.sourcing;

import com.alibaba.fastjson.JSON;
import com.github.ontio.account.Account;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.ontology.sourcing.service.util.ChainService;
import com.ontology.sourcing.util.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/application-local.properties")
public class ContractTests01 {

    @Autowired
    ChainService chainService;


    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    // https://github.com/punica-box/punica-init-default-box/blob/master/contracts/hello_ontology.py
    // AVM字节码
    private String contractCode = "013ec56b6a00527ac46a51527ac46a00c3046e616d659c640900658e076c7566616a00c30568656c6c6f9c6424006a51c3c0519e640700006c7566616a51c300c36a52527ac46a52c36551076c7566616a00c3097465737448656c6c6f9c646c006a51c3c0559e640700006c7566616a51c300c36a53527ac46a51c351c36a54527ac46a51c352c36a55527ac46a51c353c36a56527ac46a51c354c36a57527ac46a53c36a54c36a55c36a56c36a57c354795179567275517275537952795572755272756553066c7566616a00c308746573744c6973749c6424006a51c3c0519e640700006c7566616a51c300c36a58527ac46a58c365e8056c7566616a00c30e746573744c697374416e645374729c6451006a51c351c176c9681553797374656d2e52756e74696d652e4e6f74696679616a51c3c0529e640700006c7566616a51c300c36a58527ac46a51c351c36a56527ac46a58c36a56c37c6528056c7566616a00c30e746573745374727563744c6973749c6431006a51c3681553797374656d2e52756e74696d652e4e6f74696679616a51c300c36a59527ac46a59c365a7046c7566616a00c314746573745374727563744c697374416e645374729c6432006a51c3c0529e640700006c7566616a51c300c36a59527ac46a51c351c36a56527ac46a59c36a56c37c65fa036c7566616a00c307746573744d61709c6416006a51c300c36a52527ac46a52c3655b036c7566616a00c30a746573744765744d61709c6424006a51c3c0519e640700006c7566616a51c300c36a5a527ac46a5ac365b1026c7566616a00c30c746573744d6170496e4d61709c6416006a51c300c36a52527ac46a52c365c2016c7566616a00c30f746573744765744d6170496e4d61709c6424006a51c3c0519e640700006c7566616a51c300c36a5a527ac46a5ac365e3006c7566616a00c30d7472616e736665724d756c74699c6416006a51c300c36a5b527ac46a5bc3650b006c756661006c756659c56b6a00527ac4006a52527ac46a00c3c06a53527ac4616a52c36a53c39f6473006a00c36a52c3c36a51527ac46a52c351936a52527ac46a51c3c0539e6420001b7472616e736665724d756c746920706172616d73206572726f722ef0616a51c300c36a51c351c36a51c352c35272652900009c64a2ff157472616e736665724d756c7469206661696c65642ef06288ff616161516c756656c56b6a00527ac46a51527ac46a52527ac4516c756657c56b6a00527ac4681953797374656d2e53746f726167652e476574436f6e7465787461086d61705f6b6579327c681253797374656d2e53746f726167652e476574616a51527ac40f746573744765744d6170496e4d61706a51c352c176c9681553797374656d2e52756e74696d652e4e6f74696679616a51c3681a53797374656d2e52756e74696d652e446573657269616c697a65616a52527ac46a52c36a00c3c36c756659c56b6a00527ac46a00c36a51527ac46a51c3681853797374656d2e52756e74696d652e53657269616c697a65616a52527ac4076d6170496e666f6a52c352c176c9681553797374656d2e52756e74696d652e4e6f74696679616a51c3036b6579c3681853797374656d2e52756e74696d652e53657269616c697a65616a53527ac4681953797374656d2e53746f726167652e476574436f6e7465787461086d61705f6b6579326a53c35272681253797374656d2e53746f726167652e507574616a52c36c756656c56b6a00527ac4681953797374656d2e53746f726167652e476574436f6e7465787461076d61705f6b65797c681253797374656d2e53746f726167652e476574616a51527ac46a51c3681a53797374656d2e52756e74696d652e446573657269616c697a65616a52527ac46a52c36a00c3c36c756657c56b6a00527ac46a00c36a51527ac46a51c3681853797374656d2e52756e74696d652e53657269616c697a65616a52527ac4681953797374656d2e53746f726167652e476574436f6e7465787461076d61705f6b65796a52c35272681253797374656d2e53746f726167652e507574616a51c3036b6579c36c756659c56b6a00527ac46a51527ac414746573745374727563744c697374416e645374726a00c36a51c353c176c9681553797374656d2e52756e74696d652e4e6f746966796100c176c96a52527ac46a52c36a00c3c86a52c36a51c3c86a52c36c756655c56b6a00527ac40e746573745374727563744c6973746a00c352c176c9681553797374656d2e52756e74696d652e4e6f74696679616a00c36c756659c56b6a00527ac46a51527ac40e746573744c697374416e645374726a00c36a51c353c176c9681553797374656d2e52756e74696d652e4e6f746966796100c176c96a52527ac46a52c36a00c3c86a52c36a51c3c86a52c36c756655c56b6a00527ac40b746573744d73674c6973746a00c352c176c9681553797374656d2e52756e74696d652e4e6f74696679616a00c36c75665fc56b6a00527ac46a51527ac46a52527ac46a53527ac46a54527ac4097465737448656c6c6f6a00c36a51c36a52c36a53c36a54c356c176c9681553797374656d2e52756e74696d652e4e6f746966796100c176c96a55527ac46a55c36a00c3c86a55c36a51c3c86a55c36a52c3c86a55c36a53c3c86a55c36a54c3c86a55c36c756654c56b6a00527ac46a00c36c756653c56b046e616d656c7566";

    // 合约哈希/合约地址/contract codeAddr
    private String codeAddr = Address.AddressFromVmCode(contractCode).toHexString();
    // 16edbe366d1337eb510c2ff61099424c94aeef02
    // TODO ABI

    @Test
    public void example01() throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("hello".getBytes());

        List args = new ArrayList();
        args.add("some message ...");

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        System.out.println(params);

        //
        System.out.println(codeAddr);  // 16edbe366d1337eb510c2ff61099424c94aeef02
        System.out.println(Helper.reverse(codeAddr));  // 02efae944c429910f62f0c51eb37136d36beed16

        //
        String result = invokeContractPreExec(Helper.reverse(codeAddr), null, params, payerAccount, chainService.ontSdk.DEFAULT_GAS_LIMIT, GlobalVariable.DEFAULT_GAS_PRICE);
        System.out.println(result);
        // {"Notify":[],"State":1,"Gas":20000,"Result":"736f6d65206d657373616765202e2e2e"}

        String s1 = JSON.parseObject(result).getString("Result");
        System.out.println(s1);
        // 736f6d65206d657373616765202e2e2e

        byte[] s2 = Helper.hexToBytes(s1);

        String s3 = new String(s2);
        System.out.println(s3);
        // some message ...
    }

    public String invokeContractPreExec(String codeAddr, String method, byte[] params, Account payerAcct, long gaslimit, long gasprice) throws Exception {
        if (payerAcct == null) {
            throw new SDKException("params should not be null");
        }
        if (gaslimit < 0 || gasprice < 0) {
            throw new SDKException("gaslimit or gasprice should not be less than 0");
        }

        Transaction tx = chainService.ontSdk.vm().makeInvokeCodeTransaction(codeAddr, method, params, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        System.out.println(tx);
        // com.github.ontio.core.payload.InvokeCode@fecc2faa

        //
        chainService.ontSdk.addSign(tx, payerAcct);
        //  MultiSign
        //        Account account = new Account(Helper.hexToBytes("274b0b664d9c1e993c1d62a42f78ba84c379e332aa1d050ce9c1840820acee8b"),SignatureScheme.SHA256WITHECDSA);
        //        Account account2 = new Account(Helper.hexToBytes("67ae8a3731709d8c820c03b200b9552ec61e6634cbcaf8a6a1f9d8f9f0f608"),SignatureScheme.SHA256WITHECDSA);
        //        ontSdk.addMultiSign(tx,2,new byte[][]{account.serializePublicKey(),account2.serializePublicKey()},account);
        //        ontSdk.addMultiSign(tx,2,new byte[][]{account.serializePublicKey(),account2.serializePublicKey()},account2);

        //
        Object result = chainService.ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        // result = ontSdk.getConnect().sendRawTransaction(tx.toHexString());  // TODO 返回的是 boolean

        //
        String txhash = tx.hash().toString();
        System.out.println(txhash);
        // 8ec3eba94833e652d794a20eba4136d08bbee8f192834aac63bdd34bf06bb391

        //
        System.out.println(result);
        // {"Notify":[],"State":1,"Gas":20000,"Result":"736f6d65206d657373616765202e2e2e"}

        return result.toString();
    }

}
