pragma solidity >=0.4.22 <0.6.0;

contract OperatorContract {
    struct Passenger {
        string name;
        string fiscalCode;
        string phoneNumber;
        address ethWalletAddr;
    }
    mapping (address => Passenger) passengers;
    address[] public passengerContractAddresses;
    
    mapping (string => address) fcodeToAddressMap;
    string[] private fiscalCodes; 
    
    address payable operatorWalletAddress;
    uint public ticketFee; //in wei
    uint public transitPassFee; //in wei
    uint public tariff;
    uint public tariffHigh;
    
    constructor(uint _ticketFee, uint _transitPassFee, uint _tariff, uint _tariffHigh) public {
        operatorWalletAddress = 0xC03651bc82064D114f6E9af588A362fE14FFf5A8;
        ticketFee = _ticketFee;
        transitPassFee = _transitPassFee;
        tariff = _tariff;
        tariffHigh = _tariffHigh;
    }

    modifier restricted() {
        require(msg.sender == operatorWalletAddress); 
        _;
    }

    function setTicketFee(uint newTicketFee) public restricted {
        ticketFee = newTicketFee;     
    }
    
    function setTransitPassFee(uint newTransitPassFee) public restricted {
        transitPassFee = newTransitPassFee;     
    }
    
    function setPassenger(address _contractAddr, address _ethWalletAddress, string memory _name, string memory _phoneNumber, string memory _fiscalCode) internal {
        Passenger memory newPassenger = passengers[_contractAddr];    
    
        newPassenger.name = _name;
        newPassenger.fiscalCode = _fiscalCode;
        newPassenger.phoneNumber = _phoneNumber;
        newPassenger.ethWalletAddr = _ethWalletAddress;
    
        passengerContractAddresses.push(_contractAddr) -1;
    }   
    

    function generatePassengerContract(string memory _name, string memory _phoneNumber, string memory _fiscalCode) public {  
        PassengerContract newPassCon = new PassengerContract(ticketFee, msg.sender, operatorWalletAddress, transitPassFee, tariff, tariffHigh);
        setPassenger(address(newPassCon), msg.sender, _name, _phoneNumber, _fiscalCode);
        fcodeToAddressMap[_fiscalCode] = address(newPassCon);
    }
    
    function checkBalance() public restricted view returns(uint256) {
        return address(operatorWalletAddress).balance;
    }
    
    function getPassengerContractAddressList() public restricted view returns(address[] memory) {
        return passengerContractAddresses;
    }
    
    function getUniquePassengerContractAddress(string memory _fiscalCode) public view returns(address) {
        return fcodeToAddressMap[_fiscalCode];
    }
}

contract PassengerContract {
    OperatorContract oc;
    
    struct newTravelStruct {
        string startLoc;
        uint startTime;
        string endLoc;
        uint endTime;
        bool ticketFeePaid;
        bool feePaidWithPass;
    }
    
    newTravelStruct[] public travels;
    
    string private tempStartLoc;
    uint private tempStartTime;
    bool private tempFeePaidStatus;
    bool private tempPaidwithPassStatus;
    
    address public passengerAddr; //passenger wallet address
    address payable operatorAddr; //mobility provider wallet address
    uint public currentDeposit;
    uint public ticketFee; //in ethers
    uint public transitPassExpiryTime;
    uint public transitPassValidityPeriod;
    uint public freezeDeposit = 0;
    uint public transitPassFee;
    uint public totalFee; // fee for operator to retrieve
    uint public totalFeeUnpaid; // fee for operator to impose as fine
    uint public tariff; 
    uint public tariffHigh;
    uint public totalTravelTime = 0;
    uint public totalTravelTimeUnpaid = 0;
    
    constructor(uint _ticketFee, address _passengerAddr, address payable _operatorAddr, uint _transitPassFee,
        uint _tariff, uint _tariffHigh) public {
        ticketFee = _ticketFee; 
        passengerAddr = _passengerAddr;
        operatorAddr = _operatorAddr; //Known address of operator1
        transitPassFee = _transitPassFee;
        tariff = _tariff;
        tariffHigh = _tariffHigh;
        transitPassExpiryTime = 0;
        transitPassValidityPeriod = 2592000; // 30 days
    }
    
    modifier onlyOwner() {
        require(msg.sender == passengerAddr);
        //require(address(this).balance > ticketFee*1 wei);
        _;
    }
    
    modifier onlyOwnerForTransitPass() {
        require(msg.sender == passengerAddr);
        require(address(this).balance > transitPassFee*1 wei);
        _;
    }
    
    modifier onlyMobProvider() {
        require(msg.sender == operatorAddr);
        _;
    }
    
    function depositSum() public payable {
        currentDeposit = msg.value;
    }
    
    function payTicketFee(uint _startTime, string memory _startLoc) public onlyOwner {
        if (now <= transitPassExpiryTime) {
            tempStartTime =  _startTime;
            tempStartLoc = _startLoc;
            tempFeePaidStatus = true;
            tempPaidwithPassStatus = true;
        }
        else if ((address(this).balance - freezeDeposit) > ticketFee*1 wei) {
            freezeDeposit += ticketFee * 1 wei;
            
            tempStartTime =  _startTime;
            tempStartLoc = _startLoc;
            tempFeePaidStatus = true;
            tempPaidwithPassStatus = false;
        }
        else {
            tempStartTime =  _startTime;
            tempStartLoc = _startLoc;
            tempFeePaidStatus = false;
            tempPaidwithPassStatus = false;
        }
    }
    
    function updateTravelStruct(uint _endTime, string memory _endLoc) public onlyOwner {
        travels.push(newTravelStruct({startLoc: tempStartLoc, startTime: tempStartTime, 
            endLoc: _endLoc, endTime: _endTime, ticketFeePaid: tempFeePaidStatus, feePaidWithPass: tempPaidwithPassStatus}));
        uint travelTime = _endTime - tempStartTime;
        if (tempFeePaidStatus) {
            totalTravelTime = totalTravelTime + travelTime;
        } else {
            totalTravelTimeUnpaid = totalTravelTimeUnpaid + travelTime;
        }
        
    }    
    
    function renewTransitPass() public payable onlyOwnerForTransitPass {
        operatorAddr.transfer(transitPassFee*1 wei);
        transitPassExpiryTime = now + transitPassValidityPeriod;
    }
    
    function retrieveDepositSum() public payable onlyMobProvider {
        totalFee = totalTravelTime * tariff;
        totalFeeUnpaid = totalTravelTimeUnpaid * tariffHigh; //use this to fine the passenger
        
        totalTravelTime = 0;
        totalTravelTimeUnpaid = 0;
        
        if (freezeDeposit >= totalFee) {
            operatorAddr.transfer(totalFee); 
            freezeDeposit = 0;
        }
        else {
            operatorAddr.transfer(freezeDeposit);
            totalFeeUnpaid = 2 * (totalFee - freezeDeposit);
            freezeDeposit = 0; 
        }
    }
    
    function checkBalance() public view returns(uint256) {
        return address(this).balance;
    }
    
    function payRemainingFee() public payable onlyOwner {
        if (address(this).balance > totalFeeUnpaid) {
            operatorAddr.transfer(totalFeeUnpaid);
            totalFeeUnpaid = 0;
        } 
        else {
            totalFeeUnpaid = totalFeeUnpaid - address(this).balance;
            operatorAddr.transfer(address(this).balance);
        }
    }   
}