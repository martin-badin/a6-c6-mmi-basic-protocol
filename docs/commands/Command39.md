```markdown
# Command ID 39 — Clear / fill area

This file describes `0x39` frames used to clear or fill rectangular areas
of the display.

Summary

- Direction: device -> host
- Purpose: clear or fill a rectangular area on the display
- Key fields: color type, X, Y, height, width

Frame structure (example):

| ID   | LEN  | PLC  | ?         | Color | X    | Y    | height | width | FLC  |
| ---- | ---- | ---- | --------- | ----- | ---- | ---- | ------ | ----- | ---- |
| 0x39 | 0x08 | 0xF7 | 0x05 0x01 | 0x02  | 0x1B | 0x21 | 0x01   | 0x0B  | 0xF0 |

Color values

- `0x00` — red color
- `0x02` — black color

Rendering semantics

- The command sets every pixel in the target rectangle to the requested
  color. There is no per-pixel bitmap — the payload contains coordinates and
  dimensions only.

Notes & tips

- Use `color` values consistently with other renderers (see `0x31` rules for
  multi-layer semantics). `0x39` is a rectangle fill/clear and does not
  contain bitmap bytes.

See also

- `commands/Command39.kt` — parsing & implementation details
- `docs/commands/Command31.md` — pixel-block frames with bitmap payloads
```
