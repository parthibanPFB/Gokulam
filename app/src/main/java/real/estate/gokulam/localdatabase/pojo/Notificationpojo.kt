package real.estate.gokulam.localdatabase.pojo

import java.sql.Timestamp

class  Notificationpojo{
    var sender_id: String=""
    var sendername: String=""
    var channel_url: String=""
    var message: String=""
    var channelname: String=""
    var id:Int=0
    var timestamp:Timestamp?=null

    constructor( sender_id: String,  sendername: String,  channel_url: String,  message: String , channelname: String){
        this.sender_id =sender_id
        this.sendername =sendername
        this.channel_url=channel_url
        this.message=message
        this.channelname=channelname
    }
    constructor(id:Int,sender_id: String,  sendername: String,  channel_url: String,  message: String ,channelname: String){
        this.id =id
        this.sender_id =sender_id
        this.sendername =sendername
        this.channel_url=channel_url
        this.message=message
        this.channelname=channelname
    }

    constructor(id:Int,sender_id: String,channel_url: String,message: String){
        this.id =id
        this.sender_id =sender_id
        this.channel_url=channel_url
        this.message=message

    }


}