package fr.elecomte.garden.services.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.elecomte.garden.services.model.DataRecord;

/**
 * @author elecomte
 * @since 0.1.0
 */
@RestController
@RequestMapping(Constants.API_ROOT + "/data")
public class DataRestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataRestService.class);

	/**
	 * @return
	 */
	@RequestMapping(value = "/record", method = POST)
	public void addRecord(@RequestBody DataRecord record) {
		LOGGER.info("Process record from node {} with payload {}", record.getNodeId(), record.getPayload());
	}

}
