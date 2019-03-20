package real.estate.gokulam.localdatabase.pojo

class MessagePojo {
    var id:Int=0
    var messageID:Long=0
    var message:String=""
    var payLoad:ByteArray?=null
    var timestamp:Long=0
    var channelurl:String=""

    constructor(messageID:Long, payLoad: ByteArray, timestamp:Long, channelurl:String){
        this.channelurl =channelurl
        this.messageID = messageID
        this.payLoad  =payLoad
        this.timestamp=timestamp
    }

    constructor(id:Int,messageID:Long, payLoad: ByteArray, timestamp:Long, channelurl:String){
        this.channelurl =channelurl
        this.messageID = messageID
        this.payLoad  =payLoad
        this.timestamp=timestamp
        this.id=id
    }
}