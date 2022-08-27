package io.github.droidkaigi.confsched2022.zipline

import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule

class AndroidScheduleModifier : ScheduleModifier {
    override suspend fun modify(schedule: DroidKaigiSchedule): DroidKaigiSchedule {
        return schedule
    }
}