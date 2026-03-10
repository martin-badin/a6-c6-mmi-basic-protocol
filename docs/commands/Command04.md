```markdown
# Command ID 04 — Display resolution / info

Short description

This file documents the `0x04` frame that advertises the display's static
information (resolution and identifiers). It is typically emitted during
initialization or mode changes.

Summary

- Direction: device -> host
- Purpose: report display geometry and identification fields
- Key fields: display height (2 bytes), display width (2 bytes), ID bytes

Frame structure (observed example)

| ID   | LEN  | PLC  | Payload (example)                                 | CRC  |
| ---- | ---- | ---- | ------------------------------------------------- | ---- |
| 0x04 | 0x0B | 0xF4 | 0x02 0x53 0x10 0x31 0x30 0x00 0x50 0x00 0xE0 0x11 | 0x1A |

Fields

- Identification bytes: vendor/model identifiers (e.g. `0x02 0x53 0x10 0x31 0x30`).
- Display height: 2-byte big-endian (example `0x00 0x50` -> 80).
- Display width: 2-byte big-endian (example `0x00 0xE0` -> 224).

Notes & tips

- The height/width fields are observed as big-endian in captured frames.
- This frame is useful to determine layout constraints for `0x31` pixel
  block frames.

See also

- `commands/Command04.kt` — parsing implementation in this repository
- [docs/commands/Command31.md](docs/commands/Command31.md) — pixel-block frames
  that depend on display geometry
```
