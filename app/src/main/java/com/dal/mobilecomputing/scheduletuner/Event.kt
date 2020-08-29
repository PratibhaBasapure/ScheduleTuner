package com.dal.mobilecomputing.scheduletuner

class Event {

        companion object Factory {
            fun create(): Event = Event()
        }

        var objectId: String? = null
        var eventName: String? = null
        var done: Boolean? = false
        var startDate:String? =null
        var startTime:String? =null
        var endDate:String? =null
        var endTime:String? =null

    }
