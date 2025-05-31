package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.models.dtos.requests.ExampleRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ExampleResponseDTO;
import com.oo2.grupo3.models.entities.Example;
import com.oo2.grupo3.repositories.IExampleRepository;
import com.oo2.grupo3.services.interfaces.IExampleService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class ExampleServiceImp implements IExampleService {

    private final IExampleRepository exampleRepository;
    private final ModelMapper modelMapper;

    public ExampleServiceImp(IExampleRepository exampleRepository, ModelMapper modelMapper) {
        this.exampleRepository = exampleRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ExampleResponseDTO save(ExampleRequestDTO exampleRequestDTO) {
        Example example = modelMapper.map(exampleRequestDTO, Example.class);
        Example saved = exampleRepository.save(example);
        return modelMapper.map(saved, ExampleResponseDTO.class);
    }

    @Override
    public ExampleResponseDTO findById(Integer id) {
        Example example = exampleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Example with id {0} not found",id)));
        return modelMapper.map(example, ExampleResponseDTO.class);
    }

    @Override
    public ExampleResponseDTO findByIdNotDeleted(Integer id) {
        Example example = exampleRepository.findByIdAndSoftDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Example with id {0} not found",id)));
        return modelMapper.map(example, ExampleResponseDTO.class);
    }

    @Override
    public Page<ExampleResponseDTO> findAll(Pageable pageable) {
        return exampleRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, ExampleResponseDTO.class));

    }

    @Override
    public Page<ExampleResponseDTO> findAllNotDeleted(Pageable pageable) {
        return exampleRepository.findAllBySoftDeletedFalse(pageable)
                .map(entity -> modelMapper.map(entity, ExampleResponseDTO.class));
    }

    @Override
    public ExampleResponseDTO update(Integer id, ExampleRequestDTO exampleRequestDTO) {
        Example example = exampleRepository.findByIdAndSoftDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Example with id {0} not found",id)));

        example.setName(exampleRequestDTO.getName());
        example.setPrice(exampleRequestDTO.getPrice());
        Example updated = exampleRepository.save(example);
        return modelMapper.map(updated, ExampleResponseDTO.class);
    }

    @Override
    public void deleteById(Integer id) {
        Example example = exampleRepository.findByIdAndSoftDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Example with id {0} not found",id)));

        example.setSoftDeleted(true);
        exampleRepository.save(example);
    }

    @Override
    public ExampleResponseDTO restoreById(Integer id) {
        Example example = exampleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Example with id {0} not found",id)));

        if (!example.isSoftDeleted()) {
            throw new IllegalStateException(MessageFormat.format("Example with id {0} is not deleted",id));
        }

        example.setSoftDeleted(false);
        Example restored = exampleRepository.save(example);
        return modelMapper.map(restored, ExampleResponseDTO.class);
    }
}