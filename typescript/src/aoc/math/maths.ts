export namespace Maths {
	export namespace Arithmetics {
		export function gcd(a: number, b: number): number {
			let aCopy = a;
			let bCopy = b;
			while (bCopy !== 0) {
				const temp = bCopy;
				bCopy = aCopy % bCopy;
				aCopy = temp;
			}
			return aCopy;
		}

		export function lcm(numbers: number[]): number {
			if (numbers.length < 2) {
				throw new Error("At least two numbers are required to find the LCM.");
			}
			const lcmImpl = (a: number, b: number) => (a * b) / gcd(a, b);

			let tempLcm = numbers[0];
			for (let i = 1; i < numbers.length; i++) {
				tempLcm = lcmImpl(tempLcm, numbers[i]);
			}
			return tempLcm;
		}
	}
}