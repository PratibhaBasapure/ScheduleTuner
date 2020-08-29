package com.dal.mobilecomputing.scheduletuner


class Goals {

    companion object Factory {
        fun create(): Goals = Goals()
    }

    var objectId: String? = null
    var goalName: String? = null
    var startDate:String? =null
    var totalhours: String?=null
    var howoften:String? =null


}