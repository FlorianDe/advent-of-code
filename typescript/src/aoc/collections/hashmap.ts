import {Hash, IHashable} from "./hash";

export class HashMap<K extends IHashable, V> extends Map<K, V> {
	private readonly _map;

	constructor(entries?: readonly (readonly [K, V])[] | null) {
		super();
		this._map = new Map<Hash, {key: K, value: V}>(entries ? entries.map(([key, value]) => [key.hashCode(),{key, value}]): undefined);
	}

	public get(key: K): V | undefined {
		return this._map.get(key.hashCode())?.value;
	}
	public has(key: K): boolean {
		return this._map.has(key.hashCode());
	}
	public set(key: K, value: V): this {
		this._map.set(key.hashCode(), {key, value});
		return this;
	}
	public delete(key: K): boolean {
		return this._map.delete(key.hashCode());
	}
	public get size(): number {
		return this._map.size;
	}
	public forEach(callbackFn: (value: V, key: K, map: Map<K, V>) => void, thisArg?: any): void {
		this._map.forEach((entry ): void => callbackFn(entry.value, entry.key, thisArg));
	}
	public entries(): IterableIterator<[K, V]> {
		return Array.from(this._map.values()).map((entry): [K, V] => [entry.key, entry.value]).values();
	}
	public keys(): IterableIterator<K> {
		return Array.from(this._map.values()).map((entry) => entry.key).values();
	}
	public values(): IterableIterator<V> {
		return Array.from(this._map.values()).map((entry) => entry.value).values();
	}
	public clear(): void {
		this._map.clear();
	}
}