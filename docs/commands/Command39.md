# Command ID 39 — Clear / fill area

Short description

Documents `0x39` frames used by the device to clear or fill rectangular areas
of the display.

Summary

- Direction: device -> host
- Purpose: clear or fill a rectangular area on the display
- Key fields: color type, X, Y, height, width

Frame structure (example)

| ID   | LEN  | PLC  | Payload (example)                  | CRC  |
| ---- | ---- | ---- | ---------------------------------- | ---- |
| 0x39 | 0x08 | 0xF7 | 0x05 0x01 0x02 0x1B 0x21 0x01 0x0B | 0xF0 |

Color values

- `0x00` — red
- `0x02` — black

Rendering semantics

- Sets every pixel in the target rectangle to the requested color. There is
  no per-pixel bitmap; payload contains coordinates and dimensions only.

Fields

- Header / reserved bytes: `payload[0..1]` — observed `0x05 0x01`; likely flags or message sub-header.
- Color: `payload[2]` — fill color (e.g. `0x02` = black).
- X: `payload[3]` — X coordinate (single byte).
- Y: `payload[4]` — Y coordinate (single byte).
- Height: `payload[5]` — rectangle height in pixels (single byte).
- Width: `payload[6]` — rectangle width in pixels (single byte).

Notes & tips

- Use color semantics consistently with `0x31` rules for multi-layer
  interpretation. `0x39` is a rectangle fill/clear and contains no bitmap data.

See also

- `commands/Command39.kt` — parsing & implementation details
- [docs/commands/Command31.md](docs/commands/Command31.md) — pixel-block frames
  with bitmap payloads
