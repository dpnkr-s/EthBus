# EthBus

EthBus is a decentralized app (Dapp) for Android which enables automatic payments for public transportation service based on Ethereum. It is at proof-of-concept stage right now and can be extended to support other Mobility-as-a-Service (MaaS) platforms like shared bikes, cars etc.

The idea is to simplify the tasks performed by users of an urban bus transportation service. App features include: 

- Detection of user’s proximity to a bus stop
- Detection of user’s presence on the bus
- Payment of fee proportional to travel duration

  + using automated smart contract logic
  + via cryptocurrency tokens
  
Ethereum blockchain (Rinkeby testnet) is used for transactions, travel data storage and smart contract for *virtual travel card* is also implemented using Ethereum smart contracts. System also includes an online dashboard for bus service operator.

### System overview

<img width="600" alt="Screenshot 2023-02-23 at 12 12 04 AM" src="https://user-images.githubusercontent.com/25234772/220766257-d760d366-bf8d-4ca0-8149-f9d055bdfabf.png">

### Smart contract overview

<img width="600" alt="Screenshot 2023-02-23 at 12 12 04 AM" src="https://user-images.githubusercontent.com/25234772/220771547-dd0b333f-ad9b-4f3c-b44f-345e91f8f97a.jpg">

### App functionality
- Create/Link existing Ethereum wallet account to the app
- Create a new smart contract to register for the service
- Detect user’s activity state to automatically determine its presence on the bus
- Trigger transactions on the blockchain to anonymously pay ticket fee and store travel data
- Cryptocurrency tokens allow for micro-payments, can enable various pay-per-use business models

*format of user's encrypted travel data stored on blockchain is shown below, this provides functionality while maintaining privacy and anonymity*

<img width="825" alt="Screenshot 2023-02-23 at 3 26 20 AM" src="https://user-images.githubusercontent.com/25234772/220768975-21423a47-617f-4e55-a5c2-e7511c28c030.png">

### Prototype test observations

- Prototype app is able to perform the desired tasks with transaction confirmation latency ~ 15 sec
- Ethereum transaction costs can be mitigated
  + with efficient computation of gas costs 
  + or, using alternative public blockchain platforms like EOS, Cardano etc. 
  + or, enterprise blockchain solutions like HyperLedger suite
- Improved AI algorithms can be used to correctly predict the user’s presence on the bus without external APIs, which can pose a security risk
  + alternative approach can use a single on board unit (OBU) on the bus which acts as an oracle for blockchain network
