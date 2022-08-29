func calculateMinute(startSeconds: Int, endSeconds: Int) -> Int {
    let startMinute = startSeconds / 60
    let endMinute = endSeconds / 60

    return endMinute - startMinute
}
