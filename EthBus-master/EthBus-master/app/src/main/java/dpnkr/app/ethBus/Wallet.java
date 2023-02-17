package dpnkr.app.ethBus;

import android.os.Environment;
import android.util.Log;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by subhod_i on 16-03-2018.
 */

public class Wallet {

    //default wallet file
    private String fileName = "UTC--2019-02-18T17-05-54.311--235e48702af1652a0b4e0f6a0b9c4ddd34300aeb.json";
    private String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();

    public String createWallet(String password) throws Exception {
        //String pathTemp = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        String fileNameTemp = WalletUtils.generateLightNewWalletFile(password, new File(path));
        return path + "/" + fileNameTemp;
    }

    public Credentials loadCredentials(String password) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(
                password,
                path + "/" + fileName);
        Log.i("Loading credentials", "Credentials loaded");
        return credentials;
    }

    public Web3j constructWeb3(String URL) throws IOException {
        Web3j web3 = Web3j.build(new HttpService(URL));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion;
        web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        Log.i("Web3 verison", clientVersion);
        return web3;
    }

    public String sendTransaction(Web3j web3, Credentials credentials, BigDecimal value) throws Exception {
        TransactionReceipt transferReceipt = Transfer.sendFunds(web3, credentials,
                "0xC03651bc82064D114f6E9af588A362fE14FFf5A8",  // you can put any address here
                value, Convert.Unit.ETHER)  // 1 wei = 10^-18 Ether
                .send();
        return transferReceipt.getTransactionHash();
    }

    public String createBipWallet(String passwordText) throws Exception {
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(passwordText, new File(path));
        String filename = bip39Wallet.getFilename();
        String mnemonic = bip39Wallet.getMnemonic();
        return "Success";
    }
}
