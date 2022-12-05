type Digits = 1|2|3|4|5|6|7|8|9
export type Year = `201${5|6|7|8|9}` | `20${2|3|4|5|6|7|8|9}${0|Digits}`
export type Day =  `${Digits}`| `0${Digits}` | `1${0|Digits}` | `2${0|1|2|3|4|5}`

export function assertValidDay(day: number | string | Day): asserts day is Day {
	const numDay: number = (typeof day !== 'number') ? parseInt(day, 10) : day;
	if(!Number.isInteger(numDay) || !(numDay >= 1 && numDay <= 25)){
		throw Error("The provided day is not a valid integer between 1 and 25");
	}
}

export function assertValidYear(year: number | string | Year): asserts year is Year {
	const numYear: number = (typeof year !== 'number') ? parseInt(year, 10) : year;
	if(!Number.isInteger(numYear) ||  numYear < 2015 ||  numYear > 2099){
		throw Error("The provided year is not a valid integer between 2015 and 2099");
	}
}