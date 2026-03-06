```markdown
# Command ID 0D — Acknowledge (ACK)

This file documents the short device acknowledgements observed in the
captures. `0x0D` frames commonly echo a requested block id.

Summary

- Direction: device -> host
- Purpose: acknowledge a host `0x0A` block request and indicate readiness
- Key fields: acknowledged block id (single-byte payload)

Observed characteristics

- Data length: 1 byte (single UByte)
- Semantics: the single byte appears to echo or acknowledge a previously
  requested block id (see `0x0A` requests in `example/data.csv`).

Typical sequence

- Host sends a request: `0x0A ... <block_id> ...`
- Device replies with an ACK: `0x0D ... <block_id>`
- Device then sends the requested data block in a `0x31` (pixel block) or
  `0x39` (clear/fill) frame containing the same block id.

Frame structure (observed examples):

| ID   | LEN  | PLC  | Data | FLC  |
| ---- | ---- | ---- | ---- | ---- |
| 0x0D | 0x02 | 0xFD | 0x04 | 0xF6 |

Notes & recommendations

- Treat `0x0D` as a simple ACK / ready indicator when correlating host
  `0x0A` requests with incoming `0x31`/`0x39` payloads.
- The ACK's single-byte payload is the useful value (block id). Other CAN
  header bytes (LEN, PLC) follow transport conventions used elsewhere.

See also

- `docs/commands/Command0A.md` — host block requests
- `docs/commands/Command31.md` — pixel block payloads

If you want, I can add a small parser helper (`Command0D.kt` / JS) that
extracts the acknowledged block id and links it to the next incoming display
frames.
```
