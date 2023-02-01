package com.infy.infyinterns.api;

import java.util.List;

import com.infy.infyinterns.service.ProjectAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.exception.InfyInternException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/infyInterns")
@Validated
public class ProjectAllocationAPI
{
    @Autowired
    private ProjectAllocationService projectAllocationService;

    @Autowired
    private Environment environment;

    // add new project along with mentor details
    @PostMapping(value = "/project")
    public ResponseEntity<String> allocateProject(@RequestBody @Valid  ProjectDTO project) throws InfyInternException
    {
        Integer projectId =projectAllocationService.allocateProject(project);
        String success=environment.getProperty("API.ALLOCATION_SUCCESS") + ":" + projectId.toString();
        return new ResponseEntity<String>(success, HttpStatus.OK);


    }

    // get mentors based on idea owner
    @GetMapping(value = "mentor/{numberOfProjectsMentored}")
    public ResponseEntity<List<MentorDTO>> getMentors(@PathVariable(value = "numberOfProjectsMentored") Integer numberOfProjectsMentored) throws InfyInternException
    {
        List<MentorDTO> dtos=projectAllocationService.getMentors(numberOfProjectsMentored);
        return new ResponseEntity<List<MentorDTO>>(dtos, HttpStatus.OK);

    }

    // update the mentor of a project
    @PutMapping(value = "project/{projectId}/{mentorId}")
    public ResponseEntity<String> updateProjectMentor(@PathVariable(value = "projectId") Integer projectId,@PathVariable(value = "mentorId") @Min(value = 1000,message = "{mentor.mentorid.invalid}") @Max(value = 9999,message = "{mentor.mentorid.invalid}") Integer mentorId) throws InfyInternException
    {
        projectAllocationService.updateProjectMentor(projectId, mentorId);
        String success= environment.getProperty("API.PROJECT_UPDATE_SUCCESS");
        return new ResponseEntity<String>(success, HttpStatus.OK);

    }

    // delete a project
    @DeleteMapping(value = "project/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer projectId) throws InfyInternException
    {
        projectAllocationService.deleteProject(projectId);
        String success=environment.getProperty("API.PROJECT_DELETE_SUCCESS");
        return new ResponseEntity<String>(success, HttpStatus.OK);

    }

}
