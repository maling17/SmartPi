package com.example.smartpi.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName


data class TeacherScheduleSlotModel(

    @field:SerializedName("availability")
    val availability: List<AvailabilitySlotItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
)


data class AvailabilitySlotItem(

    @field:SerializedName("teacher_id")
    val teacherId: Int? = null,

    @field:SerializedName("start")
    val start: String? = null,

    @field:SerializedName("end")
    val end: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("time_zone")
    val timeZone: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
    var isSelected: Boolean? = false

) {

    class ScheduleDiffUtilCallback : DiffUtil.ItemCallback<AvailabilitySlotItem>() {
        override fun areItemsTheSame(
            oldItem: AvailabilitySlotItem,
            newItem: AvailabilitySlotItem
        ): Boolean {
            return true
            // set the way how to identify if the newItem is the same as the oldItem
        }

        override fun areContentsTheSame(
            oldItem: AvailabilitySlotItem,
            newItem: AvailabilitySlotItem
        ): Boolean {
            return true
            // set the way how to identify if the newItem's contents is the same as the oldItem's contents
        }
    }

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as AvailabilitySlotItem

        if (id != other.id) {
            return false
        }
        if (teacherId != other.teacherId) {
            return false
        }
        if (start != other.start) {
            return false
        }
        if (end != other.end) {
            return false
        }
        if (status != other.status) {
            return false
        }
        if (timeZone != other.timeZone) {
            return false
        }
        if (isSelected != other.isSelected) {
            return false
        }


        return true

    }

    override fun hashCode(): Int {
        var result = teacherId ?: 0
        result = 31 * result + (start?.hashCode() ?: 0)
        result = 31 * result + (end?.hashCode() ?: 0)
        result = 31 * result + (id ?: 0)
        result = 31 * result + (timeZone?.hashCode() ?: 0)
        result = 31 * result + (status ?: 0)
        result = 31 * result + (isSelected?.hashCode() ?: 0)
        return result
    }
}
