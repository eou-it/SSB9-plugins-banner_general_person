/** *******************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package banner.general.person


class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
