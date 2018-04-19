package main

// Gardn-manager : backend prototype
// Work in progress
// TODO : add headers, doc ...

import (
	"log"
	"net/http"
	"strconv"

	"github.com/emicklei/go-restful"
	restfulspec "github.com/emicklei/go-restful-openapi"
	"github.com/go-openapi/spec"
)

type DataResource struct {
	datas []Data
}

type CommandResource struct {
	commands []Command
}

// Routes for Data recording
//
func (d DataResource) WebService() *restful.WebService {
	ws := new(restful.WebService)
	ws.
		Path("/garden/records").
		Consumes(restful.MIME_JSON).
		Produces(restful.MIME_JSON)

	tags := []string{"records"}

	ws.Route(ws.GET("").To(d.findAllData).
		// docs
		Doc("get all data").
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Writes([]Data{}).
		Returns(200, "OK", []Data{}))

	ws.Route(ws.PUT("").To(d.recordData).
		// docs
		Doc("record a data instance").
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Reads(Data{})) // from the request

	return ws
}

// Routes for command sharing
//
func (c CommandResource) WebService() *restful.WebService {
	ws := new(restful.WebService)
	ws.
		Path("/garden/commands").
		Consumes(restful.MIME_JSON).
		Produces(restful.MIME_JSON)

	tags := []string{"commands"}

	ws.Route(ws.GET("").To(c.getActiveCommands).
		// docs
		Doc("Get all active commands").
		Metadata(restfulspec.KeyOpenAPITags, tags).
		Returns(200, "OK", []Command{}))

	return ws
}

// GET http://localhost:8080/garden/records
//
func (d *DataResource) findAllData(request *restful.Request, response *restful.Response) {
	log.Println("Listing " + strconv.Itoa(len(d.datas)) + " datas")
	response.WriteEntity(d.datas)
}

// PUT http://localhost:8080/garden/records
// {NodeId:"",Payload:""}
//
func (d *DataResource) recordData(request *restful.Request, response *restful.Response) {
	data := Data{}
	err := request.ReadEntity(&data)
	if err == nil {
		d.datas = append(d.datas, data)
		log.Println("Record data " + data.NodeID + " payload : " + data.Payload + ". Now has " + strconv.Itoa(len(d.datas)))
		response.WriteHeaderAndEntity(http.StatusCreated, data)
	} else {
		response.WriteError(http.StatusInternalServerError, err)
	}
}

// GET http://localhost:8080/garden/commands
//
func (c CommandResource) getActiveCommands(request *restful.Request, response *restful.Response) {
	log.Println("Listing " + strconv.Itoa(len(c.commands)) + " active commands")
	response.WriteEntity(c.commands)
}

func main() {
	d := DataResource{[]Data{}}
	c := CommandResource{[]Command{{"NODE1", "something", true}, {"NODE2", "something2", false}, {"NODE1", "something3", false}}}
	restful.DefaultContainer.
		Add(d.WebService()).
		Add(c.WebService())

	config := restfulspec.Config{
		WebServices: restful.RegisteredWebServices(), // you control what services are visible
		APIPath:     "/apidocs.json",
		PostBuildSwaggerObjectHandler: enrichSwaggerObject}
	restful.DefaultContainer.Add(restfulspec.NewOpenAPIService(config))

	// Optionally, you can install the Swagger Service which provides a nice Web UI on your REST API
	// You need to download the Swagger HTML5 assets and change the FilePath location in the config below.
	// Open http://localhost:8080/apidocs/?url=http://localhost:8080/apidocs.json
	http.Handle("/apidocs/", http.StripPrefix("/apidocs/", http.FileServer(http.Dir("./swagger-ui/dist-v2"))))

	// Optionally, you may need to enable CORS for the UI to work.
	cors := restful.CrossOriginResourceSharing{
		AllowedHeaders: []string{"Content-Type", "Accept"},
		AllowedMethods: []string{"GET", "PUT"},
		CookiesAllowed: false,
		Container:      restful.DefaultContainer}
	restful.DefaultContainer.Filter(cors.Filter)

	log.Printf("Get the API using http://localhost:8080/apidocs.json")
	log.Printf("Open Swagger UI using http://localhost:8080/apidocs/?url=http://localhost:8080/apidocs.json")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

func enrichSwaggerObject(swo *spec.Swagger) {
	swo.Info = &spec.Info{
		InfoProps: spec.InfoProps{
			Title:       "Garden manager Rest services",
			Description: "Resources for data recording and command management",
			Contact: &spec.ContactInfo{
				Name: "elecomte",
				URL:  "https://www.elecomte.com",
			},
			License: &spec.License{
				Name: "MIT",
				URL:  "http://mit.org",
			},
			Version: "0.0.1",
		},
	}
	swo.Tags = []spec.Tag{spec.Tag{TagProps: spec.TagProps{
		Name:        "records",
		Description: "Managing records"}}, spec.Tag{TagProps: spec.TagProps{
		Name:        "commands",
		Description: "Managing commands"}}}
}

// Data model
type Data struct {
	NodeID  string
	Payload string
}

// Command model
type Command struct {
	NodeID  string
	ToRun   string
	HasNext bool
}
