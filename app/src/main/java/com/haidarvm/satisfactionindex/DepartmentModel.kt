package com.haidarvm.satisfactionindex


data class DepartmentModel(
    val id:Int, val available: String, val name:String,
    val bg_color:String, val satisfy_img:String, val dissatisfy_img:String,
    val textService:String, val logo:String)

data class DepartmentSel(val id: Int, val deviceId: String, val available:Int)
