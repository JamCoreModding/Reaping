## Changelog

This release contains a lot of balance, and polish features, but _**will not be backported to 1.18.2!**_ Don't ask!

### Features

- Port to 1.19.
- The different tiers of reapers now have varying chances of killing the entity when used - to represent sharpness/the
  cleanness of the cut.
    - Enchanting the reaper with sharpness will affect this value.
    - A new enchantment, Curse of Bluntness, has been added, which also affects this value.
- Add a cooldown to the reaper based on its tier.
- Add a new kind of pillager, who carries a reaper and will reap any villagers it finds during a raid!
- Add tooltips to the config.
- Remove some unnecessarily granular configs.
- Publish on modrinth :).
- Add support for enchantment descriptions.

### Fixes

- Use some Architectury API features in place of custom code.
- General code clean-up to make this more maintainable.
- Don't allow reaping if the target is blocking.
- Properly name the JARs with the loader in them (this makes Modrinth not angry).
- Standardise mixin prefixes.
- Fix mixins just...not applying on Forge, no idea how I didn't notice this before.

Closed Issues: #4.

[Full Changelog](https://github.com/JamCoreModding/Reaping/compare/2.1.5...2.2.0)
