package com.infy.infyinterns.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "projectservice")
@Profile("infyInterns")
@Transactional
public class ProjectAllocationServiceImpl implements ProjectAllocationService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private MentorRepository mentorRepository;

	@Override
	public Integer allocateProject(ProjectDTO project) throws InfyInternException {
		Optional<Mentor> optional=mentorRepository.findById(project.getMentorDTO().getMentorId());
		Mentor m=optional.orElseThrow(()-> new InfyInternException("Service.MENTOR_NOT_FOUND"));
		if(m.getNumberOfProjectsMentored() >=3){
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		Project p=new Project();
		p.setIdeaOwner(project.getIdeaOwner());
		p.setProjectId(project.getProjectId());
		p.setProjectName(project.getProjectName());
		p.setReleaseDate(project.getReleaseDate());
		p.setMentor(m);
		m.setNumberOfProjectsMentored(m.getNumberOfProjectsMentored()+1);
		projectRepository.save(p);
		System.out.println("Hi");
		return p.getProjectId();
	}

	
	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException {
		List<Mentor> mentors= mentorRepository.findByNumberOfProjectsMentored(numberOfProjectsMentored);
		List<MentorDTO>dtos= new ArrayList<>();
		if (mentors.isEmpty()){
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		}
		else {

			mentors.stream().forEach((m)->{
				MentorDTO dto= new MentorDTO();
				dto.setMentorId(m.getMentorId());
				dto.setMentorName(m.getMentorName());
				dto.setNumberOfProjectsMentored(m.getNumberOfProjectsMentored());
				dtos.add(dto);

			});
		}
		return dtos;
	}


	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {
		Optional<Mentor> optional=mentorRepository.findById(mentorId);
		Mentor mentor=optional.orElseThrow(()-> new InfyInternException("Service.MENTOR_NOT_FOUND"));
		if(mentor.getNumberOfProjectsMentored() >=3){
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		Optional<Project> optional1=projectRepository.findById(projectId);
		Project project=optional1.orElseThrow(()->new InfyInternException("Service.PROJECT_NOT_FOUND"));
		project.getMentor().setNumberOfProjectsMentored(project.getMentor().getNumberOfProjectsMentored()-1);
		project.setMentor(mentor);
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()+1);

	}

	@Override
	public void deleteProject(Integer projectId) throws InfyInternException {
		Optional<Project> optional = projectRepository.findById(projectId);
		Project project = optional.orElseThrow(() -> new InfyInternException("Service.PROJECT_NOT_FOUND"));
		if (project.getMentor() == null) {
			projectRepository.delete(project);
		} else {
			Mentor mentor = project.getMentor();
			mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored());
			project.setMentor(null);
			projectRepository.delete(project);

		}
	}
}