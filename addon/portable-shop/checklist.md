# QuickShop-Hikari Shop Relocation Checklist

Remaining engineering work after splitting location/global lookup handling and adding `updateShopMap(shopId, location)`.

## 1. Core Shop Location Model

- [X] Make `ContainerShop.location` internally mutable instead of `final`.
- [X] Add an internal relocation method that **replaces** the `Location` object rather than mutating the existing instance.
- [X] Consider returning a cloned `Location` from `bukkitLocation()` so callers cannot mutate the shop's internal location.
- [X] Do not expose a normal public `setLocation()` that allows callers to bypass `ShopManager` relocation handling.

Suggested internal method:

```java
void relocate(@NotNull final Location location) {
    this.location = location.clone();
}
```

---

## 2. Manager-Level Relocation Operation

- [ ] Add a single authoritative `relocateShop(shop, newLocation)` operation to `ShopManager`.
- [ ] Capture `oldLocation` before changing the `Shop` object.
- [ ] Validate the destination before modifying runtime or database state.
- [ ] Use `removeShopLocationLookup(oldLocation)`, not the full `removeShopFromLookupTable()`, for an active relocation.
- [ ] Change the `Shop` location only through the controlled relocation lifecycle.
- [ ] Use `addShopLocationLookup(shop)` after the `Shop` points at the new location.
- [ ] Leave `allShops` unchanged during `ACTIVE -> ACTIVE` relocation.

Target API:

```java
CompletableFuture<Void> relocateShop(
    @NotNull Shop shop,
    @NotNull Location location
);
```

---

## 3. Database Consistency

- [x] Add `updateShopMap(shopId, newLocation)`.
- [ ] Use `updateShopMap()` for `ACTIVE A -> ACTIVE B` relocation.
- [ ] Keep `createShopMap(shopId, location)` for `PORTABLE -> ACTIVE` attachment.
- [ ] Keep `removeShopMap(...)` for `ACTIVE -> PORTABLE` detachment and permanent deletion.
- [ ] Verify `SHOP_MAP` has appropriate uniqueness guarantees for physical shop locations.
- [ ] Verify a shop ID can only have the appropriate single active mapping.
- [ ] Ensure `updateShopMap()` propagates database failures instead of silently leaving memory and DB out of sync.

Database lifecycle:

```text
NEW SHOP
    createShop()
    createShopMap()

ACTIVE -> ACTIVE
    updateShopMap()

ACTIVE -> PORTABLE
    removeShopMap()

PORTABLE -> ACTIVE
    createShopMap()

DELETE
    removeShopMap()
    removeShop()
```

---

## 4. Failure and Rollback Handling

- [ ] Define what happens if the DB update fails after old runtime state has been removed.
- [ ] Define what happens if physical setup at the new location fails after the DB update succeeds.
- [ ] Restore the old location lookup if relocation cannot complete.
- [ ] Restore the old internal shop location if relocation cannot complete.
- [ ] Restore the old `SHOP_MAP` record when necessary.
- [ ] Avoid exposing a partially relocated shop to other threads/tasks.
- [ ] Consider a per-shop lifecycle guard so delete, relocate, detach, and attach cannot race each other.

Example problematic state to prevent:

```text
Runtime:
Shop #123 -> Location B

Database:
Shop #123 -> Location A
```

---

## 5. Destination Validation

- [ ] Reject relocation if another shop already occupies the destination.
- [ ] Validate that the destination block is a supported `InventoryHolder`.
- [ ] Validate the destination world/location before starting relocation.
- [ ] Run relevant protection checks before changing the database.
- [ ] Run relevant permission checks before changing the database.
- [ ] Decide whether relocation across worlds is supported.
- [ ] Decide whether relocation across different `InventoryHolder` types is supported.

---

## 6. Physical Shop State

### Old Location

- [ ] Remove QuickShop PDC/state from the old `InventoryHolder`.
- [ ] Destroy old display entities.
- [ ] Remove or detach old shop signs.
- [ ] Remove other physical shop representations associated with the old location.

### New Location

- [ ] Write QuickShop PDC/state to the new `InventoryHolder`.
- [ ] Recreate display entities at the new location.
- [ ] Create/update shop signs as appropriate.
- [ ] Recreate other physical shop representations.

Do not assume existing displays/signs automatically follow:

```java
shop.relocate(newLocation);
```

They should be explicitly torn down and rebuilt.

---

## 7. Cache and Index Audit

- [x] Separate location lookup removal from complete shop removal.
- [x] Separate location lookup addition from complete shop registration.
- [x] Invalidate `shopCache` for removed locations.
- [ ] Ensure the new location's cache state is invalidated/populated appropriately.
- [ ] Audit usages of `Location.hashCode()`.
- [ ] Audit usages of `ShopChunk`.
- [ ] Audit caches/indexes using `world/x/y/z`.
- [ ] Audit cached values derived from `shop.bukkitLocation()`.
- [ ] Ensure mutable `Location` instances are not retained as map keys and later modified.

---

## 8. Folia / Region Safety

- [ ] Perform old-location Bukkit work on the old location's region thread.
- [ ] Perform new-location Bukkit work on the destination region thread.
- [ ] Keep database work asynchronous where appropriate.
- [ ] Do not manipulate Bukkit world state directly from a database `CompletableFuture` callback unless rescheduled correctly.
- [ ] Support same-region relocation.
- [ ] Support cross-region relocation.
- [ ] Support cross-world relocation if cross-world movement is allowed.

Conceptually:

```text
OLD REGION THREAD
    ↓
tear down A
    ↓
DATABASE
    ↓
update mapping
    ↓
NEW REGION THREAD
    ↓
initialize B
```

---

## 9. Relocation Events / API

- [ ] Add a `ShopRelocateEvent` or equivalent lifecycle event.
- [ ] Include the `Shop`.
- [ ] Include `oldLocation`.
- [ ] Include `newLocation`.
- [ ] Add a cancellable `PRE` phase.
- [ ] Fire `POST` only after relocation completes successfully.
- [ ] Ensure addons cannot observe a misleading half-relocated shop.

Suggested data:

```java
Shop shop;
Location oldLocation;
Location newLocation;
```

Lifecycle:

```text
PRE
 ↓
relocation
 ↓
POST
```

---

# Portable Shop Support

## 10. Portable Shop Lifecycle

Keep portable movement separate from immediate relocation.

- [ ] Add `detachShop(shop)`.
- [ ] Add `attachShop(shopId/token, location)`.
- [ ] Do **not** call normal `deleteShop()` when making a shop portable.
- [ ] Preserve the original `shopId`.
- [ ] Remove the physical `SHOP_MAP` record during detachment.
- [ ] Remove the shop from location lookup during detachment.
- [ ] Decide whether portable shops should be removed from `allShops`.
- [ ] Restore the physical `SHOP_MAP` record during attachment.
- [ ] Add the shop back to location lookup during attachment.

Target lifecycle:

```text
ACTIVE @ A
    ↓
detachShop()
    ↓
PORTABLE
    ↓
attachShop()
    ↓
ACTIVE @ B
```

The persistent identity remains:

```text
shopId = 123
```

through the entire lifecycle.

---

## 11. Portable Persistence and Loading

- [ ] Add/persist an explicit `PORTABLE` shop state.
- [x] Allow appropriate shop states to be valid without an inventory using `validWithoutInventory()`.
- [ ] Ensure portable shops do not continue through normal physical loading.
- [ ] Ensure portable shops don't attempt to locate an `InventoryHolder`.
- [ ] Ensure portable shops don't initialize signs.
- [ ] Ensure portable shops don't initialize displays.
- [ ] Update startup loading to recognize `PORTABLE`.
- [ ] Update orphan cleanup so a `PORTABLE` shop without `SHOP_MAP` is valid.
- [ ] Continue detecting `ACTIVE` shops without `SHOP_MAP` as invalid/corrupt where appropriate.

Desired persistence:

```text
ACTIVE

SHOPS
  #123 state=ACTIVE

SHOP_MAP
  #123 -> world,x,y,z
```

Portable:

```text
PORTABLE

SHOPS
  #123 state=PORTABLE

SHOP_MAP
  <none>
```

---

## 12. Portable Token / Duplication Protection

Portable ItemStack PDC:

- [x] Store `shopId`.
- [x] Store portable marker.
- [x] Store portable token.
- [x] Store owner information.
- [x] Serialize inventory contents.
- [ ] Consider adding a portable-data format version.

Server-side:

- [ ] Persist the portable token server-side.
- [ ] Store the shop's `PORTABLE` state.
- [ ] On placement, verify `shopId`.
- [ ] Verify portable token.
- [ ] Verify shop state is `PORTABLE`.
- [ ] Consume/invalidate the token when attachment succeeds.
- [ ] Reject stale portable items.
- [ ] Reject duplicated portable items.
- [ ] Handle simultaneous placement attempts for the same shop/token.

Validation should effectively require:

```text
Item.shopId == DB.shopId
AND
Item.token == DB.portableToken
AND
DB.state == PORTABLE
```

---

## 13. Portable Inventory Restoration

- [x] Serialize inventory using slot + Bukkit `ItemStack.serializeAsBytes()`.
- [x] Base64 encode serialized ItemStacks.
- [x] Store serialized slots as JSON.
- [ ] Parse and validate the complete inventory payload before calling `inventory.clear()`.
- [ ] Reject invalid slot numbers.
- [ ] Detect duplicate slot entries.
- [ ] Handle invalid Base64.
- [ ] Handle `ItemStack.deserializeBytes()` failures.
- [ ] Handle inventory-size differences.
- [ ] Decide how composite inventories such as double chests should behave.
- [ ] Ensure a malformed portable item cannot wipe the destination inventory.

Preferred restoration order:

```text
Read JSON
   ↓
Validate slots
   ↓
Decode Base64
   ↓
Deserialize all ItemStacks
   ↓
Verify destination
   ↓
Clear inventory
   ↓
Restore contents
```

---

## 14. Runtime Watchers and Scheduled Tasks

Audit systems that assume every shop is physically present.

- [ ] Ongoing fee processing.
- [ ] Display despawn/update watchers.
- [ ] Inventory polling.
- [ ] Shop maintenance tasks.
- [ ] Chunk loading/unloading handling.
- [ ] Sign update tasks.
- [ ] Metrics tasks.
- [ ] Any code iterating `allShops`.
- [ ] Any code calling `shop.bukkitLocation()` without considering shop state.
- [ ] Any code calling `shop.getInventory()` without considering shop state.

Portable shops should generally be excluded from physical-shop processing.

---

# Testing

## 15. Active Relocation Tests

- [ ] Relocate within the same chunk.
- [ ] Relocate to another chunk.
- [ ] Relocate within the same Folia region.
- [ ] Relocate across Folia regions.
- [ ] Relocate across worlds if supported.
- [ ] Attempt relocation onto an occupied shop.
- [ ] Attempt relocation onto an unsupported block.
- [ ] Force a database update failure.
- [ ] Verify rollback after database failure.
- [ ] Verify the old location no longer resolves to the shop.
- [ ] Verify the new location resolves to the shop.
- [ ] Verify `allShops` still contains the same runtime shop.
- [ ] Verify displays move correctly.
- [ ] Verify signs regenerate correctly.
- [ ] Verify QuickShop PDC moves correctly.
- [ ] Restart the server after relocation.
- [ ] Verify the relocated shop loads at its new location after restart.
- [ ] Verify the `shopId` did not change.
- [ ] Verify transaction/history records remain associated with the shop.

---

## 16. Portable Shop Tests

- [ ] Detach a shop.
- [ ] Verify `SHOPS` record remains.
- [ ] Verify `SHOP_MAP` record is removed.
- [ ] Verify shop state becomes `PORTABLE`.
- [ ] Verify old location no longer resolves to a shop.
- [ ] Restart while the shop is portable.
- [ ] Verify the portable shop survives restart.
- [ ] Place the portable shop.
- [ ] Verify the original `shopId` is restored.
- [ ] Verify inventory contents are restored.
- [ ] Verify owner is restored correctly.
- [ ] Verify signs/displays initialize.
- [ ] Verify `SHOP_MAP` is recreated at the new location.
- [ ] Verify state returns to `ACTIVE`.
- [ ] Verify transaction history remains intact.
- [ ] Duplicate the portable ItemStack.
- [ ] Place the first copy successfully.
- [ ] Verify the second copy is rejected.
- [ ] Attempt simultaneous placement of two copies.
- [ ] Verify only one attachment succeeds.

---

# Target Architecture

### Immediate Relocation

```text
ACTIVE @ A
    ↓
Validate destination
    ↓
ShopRelocateEvent PRE
    ↓
Tear down location-bound state at A
    ↓
Update SHOP_MAP
    ↓
Remove A location lookup
    ↓
Replace shop.location with B
    ↓
Add B location lookup
    ↓
Build location-bound state at B
    ↓
ShopRelocateEvent POST
    ↓
ACTIVE @ B
```

### Portable Relocation

```text
ACTIVE @ A
    ↓
detachShop()
    ↓
Remove physical state
Remove location lookup
Remove SHOP_MAP
Persist PORTABLE state + token
    ↓
PORTABLE #123
    ↓
Server can restart / time can pass
    ↓
Validate ItemStack shopId + token
    ↓
attachShop()
    ↓
Set new location
Create SHOP_MAP
Add location lookup
Restore inventory
Restore PDC
Restore signs/displays
Set ACTIVE
Consume token
    ↓
ACTIVE @ B
```

## Key Invariant

**The persistent `shopId` must never change during relocation or portable detach/attach.**

That is what keeps existing transaction history and other shop-ID-associated data attached to the same logical shop.