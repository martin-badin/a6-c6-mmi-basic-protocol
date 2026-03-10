# Command ID 55 — Brightness / display parameter

Short description

Documents `0x55` frames related to a small, three-byte parameter set observed to control display brightness or a related display parameter.

Summary

- Direction: host -> device (observed as a setting command)
- Purpose: set a small display parameter (brightness) or toggle related display mode
- Key fields: subcommand, brightness value, unknown constant (0xE0)

Frame structure (example)

| ID   | LEN  | PLC  | Payload (example) | CRC  |
| ---- | ---- | ---- | ----------------- | ---- |
| 0x55 | 0x04 | 0xFB | 0x04 0x51 0xE0    | 0x1F |

Observed examples

- 55 04 FB 04 51 E0 1F
- 55 04 FB 87 51 E0 9C
- 55 04 FB A7 51 E0 BC
- 55 04 FB C6 51 E0 DD
- 55 04 FB E6 51 E0 FD
- 55 04 FB 05 51 E0 1E
- 55 04 FB 25 51 E0 3E
- 55 04 FB 44 51 E0 5F
- 55 04 FB 64 51 E0 7F
- 55 04 FB 83 51 E0 98

Fields

- `payload[0]` — subcommand / mode byte. Captured frames commonly show `0x04` here.
- `payload[1]` — brightness value (0–255). The repository parser exposes this via `getBrightness()`.
- `payload[2]` — unknown constant, typically `0xE0` in captures.

Parsing / implementation notes

- In this codebase the `Command55` implementation constructs or parses a 3-byte payload: subcommand, brightness, unknown. The provided helper `getBrightness()` returns `payload[1]`.
- The class constructor has a convenience behaviour: when invoked with a single-byte `data` argument it appends a default brightness (`0x0F`) and the unknown byte (`0xE0`) before computing the frame checksum — see the source for details.

Notes & tips

- The middle byte is the effective brightness parameter in observed captures; try varying it across the full 0–255 range to map device response.
- The trailing `0xE0` byte appears constant across captures; it may be a flags/marker byte.
- If you need to construct frames programmatically, prefer passing the full 3-byte payload so the produced frame matches captured examples.

See also

- [commands/Command55.kt](commands/Command55.kt#L1) — parsing/constructor logic in repository
