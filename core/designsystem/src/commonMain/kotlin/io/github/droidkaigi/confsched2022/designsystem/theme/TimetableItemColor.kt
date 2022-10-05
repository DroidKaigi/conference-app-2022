package io.github.droidkaigi.confsched2022.designsystem.theme

public enum class TimetableItemColor(public val roomName: String, public val color: Long) {
    AppBar(roomName = "App Bar", color = 0xFFFE8299),
    BackDrop(roomName = "Backdrop", color = 0xFFFF592F),
    Cards(roomName = "Cards", color = 0xFFFFAF01),
    Dialogs(roomName = "Dialogs", color = 0xFF069CCF),
    Online(roomName = "Online", color = 0xFFA58CDF);

    public companion object {
        public fun colorOfRoomName(enName: String): Long {
            return values().firstOrNull { it.roomName == enName }?.color ?: AppBar.color
        }
    }
}
