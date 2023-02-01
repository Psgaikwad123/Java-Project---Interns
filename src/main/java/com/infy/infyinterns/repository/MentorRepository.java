package com.infy.infyinterns.repository;

import com.infy.infyinterns.entity.Mentor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MentorRepository extends CrudRepository<Mentor, Integer>
{
    // add methods if required
    public List<Mentor> findByNumberOfProjectsMentored(Integer numberOfProjectsMentored);
}
