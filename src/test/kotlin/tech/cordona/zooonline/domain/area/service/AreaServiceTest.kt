package tech.cordona.zooonline.domain.area.service

import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.domain.cell.service.CellService

internal class AreaServiceTest(
	@Autowired areaService: AreaService,
	@Autowired cellService: CellService,
) : PersistenceTest() {


	companion object


}

