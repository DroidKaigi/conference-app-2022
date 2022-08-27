package io.github.droidkaigi.confsched2022.designsystem.theme

enum class TimetableItemColor(val roomName: String, val color: Long) {
    AppBar(roomName = "App Bar", color = 0xFFFE8299),
    BackDrop(roomName = "Back Drop", color = 0xFFFF592F),

    // TODO: Add rooms
    Other(roomName = "xxxx", color = 0xFFFE8299);

    companion object {
        fun colorOfRoomName(enName: String): Long {
            return values().firstOrNull { it.roomName == enName }?.color ?: Other.color
        }
    }
}