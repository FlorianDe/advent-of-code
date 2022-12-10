import {IEquality, IHashable} from "../collections/hash";

export class Point2D implements IHashable, IEquality<Point2D> {
	get x(): number {
		return this._x;
	}

	get y(): number {
		return this._y;
	}

	private readonly _x: number;
	private readonly _y: number;

	constructor(x: number, y: number) {
		this._x = x;
		this._y = y;
	}

	public static fromArray(elements: number[]) {
		if (!elements || elements.length !== 2) {
			throw Error(`To create a ${this.constructor.name} from an array exactly 2 elements have to be specified.`);
		}
		const [x, y] = elements;
		return new Point2D(x, y);
	}

	public static zero(): Point2D {
		return new Point2D(0, 0);
	}

	public toArray(): number[] {
		return [this._x, this._y];
	}

	public add(other: Point2D): Point2D {
		return new Point2D(this._x + other._x, this._y + other._y);
	}

	public sub(other: Point2D): Point2D {
		return new Point2D(this._x - other._x, this._y - other._y);
	}

	public mul(other: Point2D): number {
		return this._x * other._x + this._y * other._y;
	}

	public mulScalar(scalar: number): Point2D {
		return new Point2D(scalar * this._x, scalar * this._y);
	}

	public abs(): Point2D {
		return new Point2D(Math.abs(this._x), Math.abs(this._y));
	}

	public sign(): Point2D {
		return new Point2D(Math.sign(this._x), Math.sign(this._y));
	}

	public copy(): Point2D {
		return new Point2D(this._x, this._y);
	}

	public rotate(angle: number, other?: Point2D, angleInRadians?: boolean): Point2D {
		const angleRadians = angleInRadians ? angle : angle * (Math.PI / 180);
		const s = Math.sin(angleRadians);
		const c = Math.cos(angleRadians);
		const rotationPoint = other ?? Point2D.zero();

		const pTranslated = this.sub(rotationPoint);
		const rotated = new Point2D(pTranslated._x * c - pTranslated._y * s, pTranslated._x * s + pTranslated._y * c);
		return rotated.add(rotationPoint);
	}

	equals(other: Point2D): boolean {
		return this._x === other._x && this._y === other._y;
	}

	hashCode(): string {
		return `x${this._x}y${this._y}`;
	}
}

export const DiagonalGridDirections = [
	new Point2D(1, -1),
	new Point2D(-1, -1),
	new Point2D(-1, 1),
	new Point2D(1, 1),
];

export const VerticalAndHorizontalGridDirections = [
	new Point2D(1, 0),
	new Point2D(0, 1),
	new Point2D(-1, 0),
	new Point2D(0, -1),
];

