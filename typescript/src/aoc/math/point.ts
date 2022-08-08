import {IEquality, IHashable} from "../collections/hash";

export class Point2D implements IHashable, IEquality<Point2D>{
	private readonly x: number;
	private readonly y: number;

	constructor(x: number, y: number) {
		this.x = x;
		this.y = y;
	}

	public static fromArray(elements: number[]) {
		if(!elements || elements.length !== 2){
			throw Error(`To create a ${this.constructor.name} from an array exactly 2 elements have to be specified.`);
		}
		const [x,y] = elements;
		return new Point2D(x,y);
	}
	public static zero(): Point2D {
		return new Point2D(0,0);
	}

	public toArray(): number[] {
		return [this.x, this.y];
	}

	public add(other: Point2D): Point2D {
		return new Point2D(this.x+other.x, this.y+other.y);
	}

	public sub(other: Point2D): Point2D {
		return new Point2D(this.x-other.x, this.y-other.y);
	}
	public mul(other: Point2D): number {
		return this.x*other.x+this.y*other.y;
	}
	public mulScalar(scalar: number): Point2D{
		return new Point2D(scalar*this.x, scalar*this.y);
	}

	public rotate(angle: number, other?: Point2D, angleInRadians?: boolean): Point2D{
		const angleRadians = angleInRadians ? angle : angle * (Math.PI / 180);
		const s = Math.sin(angleRadians);
		const c = Math.cos(angleRadians);
		const rotationPoint = other ?? Point2D.zero();

		const pTranslated = this.sub(rotationPoint);
		const rotated = new Point2D(pTranslated.x*c - pTranslated.y*s, pTranslated.x*s+pTranslated.y*c);
		return rotated.add(rotationPoint);
	}

	equals(other: Point2D): boolean {
		return this.x === other.x && this.y === other.y;
	}

	hashCode(): number | string {
		return `x${this.x}y${this.y}`;
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

