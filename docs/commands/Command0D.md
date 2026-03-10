```markdown
# Command ID 0D — Acknowledge (ACK)

Short description

Documents `0x0D` short acknowledgements sent by the device, commonly echoing
the requested block id.

Summary

- Direction: device -> host
- Purpose: acknowledge a host `0x0A` block request and indicate readiness
- Key fields: acknowledged block id (single-byte payload)

Frame structure (observed example)

| ID   | LEN  | PLC  | Payload (example) | CRC  |
| ---- | ---- | ---- | ----------------- | ---- |
| 0x0D | 0x02 | 0xFD | 0x04              | 0xF6 |

Fields

- Acknowledged block id: single payload byte echoing the requested block id.

Behavior & correlation

- Typical sequence: host sends `0x0A` (request) → device replies `0x0D` (ACK) →
  device sends `0x31`/`0x39` payload for the same block id.
- Treat `0x0D` as a ready/ACK indicator when correlating traffic.

See also

- [docs/commands/Command0A.md](docs/commands/Command0A.md) — host block requests
- [docs/commands/Command31.md](docs/commands/Command31.md) — pixel block payloads

Notes

- Optionally add a parser helper (`commands/Command0D.kt` / JS) to extract the
  acknowledged block id and annotate captured streams.
```
