package io.github.droidkaigi.confsched2022.designsystem.theme

enum class TimetableItemColor(val roomName: String, val color: Long) {
    AppBar(roomName = "App Bar", color = 0xFFFE8299),
    BackDrop(roomName = "Backdrop", color = 0xFFFF592F),
    Cards(roomName = "Cards", color = 0xFFFFAF01),
    Dialogs(roomName = "Dialogs", color = 0xFF069CCF),
    Online(roomName = "Online", color = 0xFFA58CDF);

    companion object {
        fun colorOfRoomName(enName: String): Long {
            return values().firstOrNull { it.roomName == enName }?.color ?: AppBar.color
        }
    }
}
