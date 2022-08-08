export type Hash = number | string;
export interface IHashable {
	hashCode():Hash;
}
export interface IEquality<T> {
	equals(other: T):boolean
}