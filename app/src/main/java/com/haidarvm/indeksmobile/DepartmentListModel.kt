package com.haidarvm.indeksmobile

import com.google.gson.annotations.SerializedName

class UsersList {
    @SerializedName("department")
    var department: List<DepartmentModel>? = null
}