package fr.elecomte.garden.services.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.elecomte.garden.services.model.Command;

/**
 * @author elecomte
 * @since 0.1.0
 */
@RestController
@RequestMapping(Constants.API_ROOT + "/command")
public class CommandRestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandRestService.class);

	/**
	 * @return
	 */
	@RequestMapping(value = "/current", method = GET)
	@ResponseBody
	public Command getCurrentCommand() {
		LOGGER.info("Request a new command");
		return null;
	}

}
