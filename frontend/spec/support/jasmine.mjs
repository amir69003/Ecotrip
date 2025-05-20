export default {
	"spec_dir": "tests",
	spec_files: [
		"**/*[sS]pec.?(m)js"
	],
	helpers: [
		"helpers/**/*.?(m)js"
	],
	env: {
		// Whether to fail a spec that ran no expectations
		failSpecWithNoExpectations: false,
		
		// Stop execution of a spec after the first expectation failure in it
		stopSpecOnExpectationFailure: false,

		// Stop execution of the suite after the first spec failure  
		stopOnSpecFailure: false,

		// Run specs in semi-random order
		random: false
	}
}
