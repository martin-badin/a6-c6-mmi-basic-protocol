````markdown
# Command ID 39 — Clear / fill area

This file describes `0x39` frames used to clear or fill rectangular areas
of the display.

Summary

- Direction: device -> host
- Purpose: clear or fill a rectangular area on the display
- Key fields: color type, X, Y, height, width

Frame structure (example):

| ID   | DLC  | CRC  | ?         | Color | X    | Y    | height | width | Order |
| ---- | ---- | ---- | --------- | ----- | ---- | ---- | ------ | ----- | ----- |
| 0x39 | 0x08 | 0xF7 | 0x05 0x01 | 0x02  | 0x1B | 0x21 | 0x01   | 0x0B  | 0xF0  |

Color values

- `0x00` — red color
- `0x02` — black color

Rendering semantics

- The command sets every pixel in the target rectangle to the requested
  color. There is no per-pixel bitmap — the payload contains coordinates and
  dimensions only.

Example renderer (from project code):

```kotlin
private fun renderCommand39(command: Command39): Errors {
    val x = command.getCoords()[0]
    val y = command.getCoords()[1]
    val height = command.getSize()[0]
    val width = command.getSize()[1]
    val color = command.getColorType().toInt()

    var hasError = Errors.NO_ERROR

    Array(height.toInt()) { it }.forEach { row ->
        Array(width.toInt()) { it }.forEach { column ->
            val xPos = x.toInt() + column
            val yPos = y.toInt() + row

            val err = if (color == 0x00) {
                // only red color
                setPixel(xPos, yPos, redColor)
            } else if (color == 0x02) {
                // only black color
                setPixel(xPos, yPos, blackColor)
            } else {
                Errors.UNKNOWN
            }

            hasError = if (err == Errors.NO_ERROR) hasError else err
        }
    }

    return hasError
}
```

Notes & tips

- Use `color` values consistently with other renderers (see `0x31` rules for
  multi-layer semantics). `0x39` is a rectangle fill/clear and does not
  contain bitmap bytes.

See also

- `commands/Command39.kt` — parsing & implementation details
- `docs/commands/Command31.md` — pixel-block frames with bitmap payloads
````
