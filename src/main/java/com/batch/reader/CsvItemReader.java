package com.batch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import com.batch.entity.Person;

public class CsvItemReader extends FlatFileItemReader<Person> {
	  public CsvItemReader() {
	        setResource(new ClassPathResource("data.csv"));  // Đọc file CSV từ resources
	        setLineMapper(new DefaultLineMapper<Person>() {{
	            setLineTokenizer(new DelimitedLineTokenizer() {{
	                setNames(new String[] { "id", "name", "age" });  // Cột CSV tương ứng với thuộc tính entity
	            }});
	            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
	                setTargetType(Person.class);
	            }});
	        }});
	    }
}
