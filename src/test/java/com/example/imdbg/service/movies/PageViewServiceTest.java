package com.example.imdbg.service.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class PageViewServiceTest {

    @Mock
    private TitleService titleService;

    private PageViewService toTest;

    @BeforeEach
    void setUp(){
        toTest = new PageViewService(titleService);

    }



    @Test
    void updatePageViewsInDb() {
        String testUri1 = "123";
        String testUri2 = "1234";
        String testSessionId1 = "testSessionId1";
        String testSessionId2 = "testSessionId2";

        toTest.incrementUniquePageView(testUri1, testSessionId1);
        toTest.incrementUniquePageView(testUri1, testSessionId2);
        toTest.incrementUniquePageView(testUri2, testSessionId2);

        toTest.updatePageViewsInDb();

        Mockito.verify(titleService).updateTitlePageViews(Long.valueOf(testUri1), 2);
        Mockito.verify(titleService).updateTitlePageViews(Long.valueOf(testUri2), 1);

        Mockito.verify(titleService, Mockito.times(2)).updateTitlePageViews(any(), any());

    }

    @Test
    void updatePageViewsInDb_throwException() {
        String testUri1 = "123";
        String testUri2 = "1234";
        String testSessionId1 = "testSessionId1";
        String testSessionId2 = "testSessionId2";

        toTest.incrementUniquePageView(testUri1, testSessionId1);
        toTest.incrementUniquePageView(testUri1, testSessionId2);
        toTest.incrementUniquePageView(testUri2, testSessionId2);

        Mockito.doThrow(new RuntimeException()).when(titleService).updateTitlePageViews(anyLong(), anyInt());

        toTest.updatePageViewsInDb();
    }
}