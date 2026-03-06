```markdown
# Command ID 04 — Display resolution / info

This file describes the `0x04` frame that advertises the display's
resolution and related static information.

Summary

- Direction: device -> host
- Purpose: report display geometry and identification fields
- Key fields: display height, display width

Frame structure (observed examples):

| ID   | LEN  | PLC  | ?                        | display height (80px) | display width (224px) | ?    | FLC  |
| ---- | ---- | ---- | ------------------------ | --------------------- | --------------------- | ---- | ---- |
| 0x04 | 0x0B | 0xF4 | 0x02 0x53 0x10 0x31 0x30 | 0x00 0x50             | 0x00 0xE0             | 0x11 | 0x1A |

Notes & tips

- The two-byte height (`0x00 0x50`) and width (`0x00 0xE0`) are big-endian
  values in captured frames and decode to 80 and 224 respectively.
- This frame is typically sent once during initialization or mode changes.

See also

- `commands/Command04.kt` — parsing implementation in this repo
- `docs/commands/Command31.md` — pixel block frames that depend on the
  display geometry
```
