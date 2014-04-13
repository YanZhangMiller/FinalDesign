package org.similarity.buaa;

import org.tools.buaa.News;

public interface Similarity {
	
	double getSimScore(News x,News y);
	
}
