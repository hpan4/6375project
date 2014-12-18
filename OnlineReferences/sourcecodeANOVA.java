001/*
002 * Licensed to the Apache Software Foundation (ASF) under one or more
003 * contributor license agreements.  See the NOTICE file distributed with
004 * this work for additional information regarding copyright ownership.
005 * The ASF licenses this file to You under the Apache License, Version 2.0
006 * (the "License"); you may not use this file except in compliance with
007 * the License.  You may obtain a copy of the License at
008 *
009 *      http://www.apache.org/licenses/LICENSE-2.0
010 *
011 * Unless required by applicable law or agreed to in writing, software
012 * distributed under the License is distributed on an "AS IS" BASIS,
013 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
014 * See the License for the specific language governing permissions and
015 * limitations under the License.
016 */
017package org.apache.commons.math3.stat.inference;
018
019import java.util.ArrayList;
020import java.util.Collection;
021
022import org.apache.commons.math3.distribution.FDistribution;
023import org.apache.commons.math3.exception.ConvergenceException;
024import org.apache.commons.math3.exception.DimensionMismatchException;
025import org.apache.commons.math3.exception.MaxCountExceededException;
026import org.apache.commons.math3.exception.NullArgumentException;
027import org.apache.commons.math3.exception.OutOfRangeException;
028import org.apache.commons.math3.exception.util.LocalizedFormats;
029import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
030import org.apache.commons.math3.util.MathUtils;
031
032/**
033 * Implements one-way ANOVA (analysis of variance) statistics.
034 *
035 * <p> Tests for differences between two or more categories of univariate data
036 * (for example, the body mass index of accountants, lawyers, doctors and
037 * computer programmers).  When two categories are given, this is equivalent to
038 * the {@link org.apache.commons.math3.stat.inference.TTest}.
039 * </p><p>
040 * Uses the {@link org.apache.commons.math3.distribution.FDistribution
041 * commons-math F Distribution implementation} to estimate exact p-values.</p>
042 * <p>This implementation is based on a description at
043 * http://faculty.vassar.edu/lowry/ch13pt1.html</p>
044 * <pre>
045 * Abbreviations: bg = between groups,
046 *                wg = within groups,
047 *                ss = sum squared deviations
048 * </pre>
049 *
050 * @since 1.2
051 */
052public class OneWayAnova {
053
054    /**
055     * Default constructor.
056     */
057    public OneWayAnova() {
058    }
059
060    /**
061     * Computes the ANOVA F-value for a collection of <code>double[]</code>
062     * arrays.
063     *
064     * <p><strong>Preconditions</strong>: <ul>
065     * <li>The categoryData <code>Collection</code> must contain
066     * <code>double[]</code> arrays.</li>
067     * <li> There must be at least two <code>double[]</code> arrays in the
068     * <code>categoryData</code> collection and each of these arrays must
069     * contain at least two values.</li></ul></p><p>
070     * This implementation computes the F statistic using the definitional
071     * formula<pre>
072     *   F = msbg/mswg</pre>
073     * where<pre>
074     *  msbg = between group mean square
075     *  mswg = within group mean square</pre>
076     * are as defined <a href="http://faculty.vassar.edu/lowry/ch13pt1.html">
077     * here</a></p>
078     *
079     * @param categoryData <code>Collection</code> of <code>double[]</code>
080     * arrays each containing data for one category
081     * @return Fvalue
082     * @throws NullArgumentException if <code>categoryData</code> is <code>null</code>
083     * @throws DimensionMismatchException if the length of the <code>categoryData</code>
084     * array is less than 2 or a contained <code>double[]</code> array does not have
085     * at least two values
086     */
087    public double anovaFValue(final Collection<double[]> categoryData)
088        throws NullArgumentException, DimensionMismatchException {
089
090        AnovaStats a = anovaStats(categoryData);
091        return a.F;
092
093    }
094
095    /**
096     * Computes the ANOVA P-value for a collection of <code>double[]</code>
097     * arrays.
098     *
099     * <p><strong>Preconditions</strong>: <ul>
100     * <li>The categoryData <code>Collection</code> must contain
101     * <code>double[]</code> arrays.</li>
102     * <li> There must be at least two <code>double[]</code> arrays in the
103     * <code>categoryData</code> collection and each of these arrays must
104     * contain at least two values.</li></ul></p><p>
105     * This implementation uses the
106     * {@link org.apache.commons.math3.distribution.FDistribution
107     * commons-math F Distribution implementation} to estimate the exact
108     * p-value, using the formula<pre>
109     *   p = 1 - cumulativeProbability(F)</pre>
110     * where <code>F</code> is the F value and <code>cumulativeProbability</code>
111     * is the commons-math implementation of the F distribution.</p>
112     *
113     * @param categoryData <code>Collection</code> of <code>double[]</code>
114     * arrays each containing data for one category
115     * @return Pvalue
116     * @throws NullArgumentException if <code>categoryData</code> is <code>null</code>
117     * @throws DimensionMismatchException if the length of the <code>categoryData</code>
118     * array is less than 2 or a contained <code>double[]</code> array does not have
119     * at least two values
120     * @throws ConvergenceException if the p-value can not be computed due to a convergence error
121     * @throws MaxCountExceededException if the maximum number of iterations is exceeded
122     */
123    public double anovaPValue(final Collection<double[]> categoryData)
124        throws NullArgumentException, DimensionMismatchException,
125        ConvergenceException, MaxCountExceededException {
126
127        final AnovaStats a = anovaStats(categoryData);
128        // No try-catch or advertised exception because args are valid
129        // pass a null rng to avoid unneeded overhead as we will not sample from this distribution
130        final FDistribution fdist = new FDistribution(null, a.dfbg, a.dfwg);
131        return 1.0 - fdist.cumulativeProbability(a.F);
132
133    }
134
135    /**
136     * Computes the ANOVA P-value for a collection of {@link SummaryStatistics}.
137     *
138     * <p><strong>Preconditions</strong>: <ul>
139     * <li>The categoryData <code>Collection</code> must contain
140     * {@link SummaryStatistics}.</li>
141     * <li> There must be at least two {@link SummaryStatistics} in the
142     * <code>categoryData</code> collection and each of these statistics must
143     * contain at least two values.</li></ul></p><p>
144     * This implementation uses the
145     * {@link org.apache.commons.math3.distribution.FDistribution
146     * commons-math F Distribution implementation} to estimate the exact
147     * p-value, using the formula<pre>
148     *   p = 1 - cumulativeProbability(F)</pre>
149     * where <code>F</code> is the F value and <code>cumulativeProbability</code>
150     * is the commons-math implementation of the F distribution.</p>
151     *
152     * @param categoryData <code>Collection</code> of {@link SummaryStatistics}
153     * each containing data for one category
154     * @param allowOneElementData if true, allow computation for one catagory
155     * only or for one data element per category
156     * @return Pvalue
157     * @throws NullArgumentException if <code>categoryData</code> is <code>null</code>
158     * @throws DimensionMismatchException if the length of the <code>categoryData</code>
159     * array is less than 2 or a contained {@link SummaryStatistics} does not have
160     * at least two values
161     * @throws ConvergenceException if the p-value can not be computed due to a convergence error
162     * @throws MaxCountExceededException if the maximum number of iterations is exceeded
163     * @since 3.2
164     */
165    public double anovaPValue(final Collection<SummaryStatistics> categoryData,
166                              final boolean allowOneElementData)
167        throws NullArgumentException, DimensionMismatchException,
168               ConvergenceException, MaxCountExceededException {
169
170        final AnovaStats a = anovaStats(categoryData, allowOneElementData);
171        // pass a null rng to avoid unneeded overhead as we will not sample from this distribution
172        final FDistribution fdist = new FDistribution(null, a.dfbg, a.dfwg);
173        return 1.0 - fdist.cumulativeProbability(a.F);
174
175    }
176
177    /**
178     * This method calls the method that actually does the calculations (except
179     * P-value).
180     *
181     * @param categoryData
182     *            <code>Collection</code> of <code>double[]</code> arrays each
183     *            containing data for one category
184     * @return computed AnovaStats
185     * @throws NullArgumentException
186     *             if <code>categoryData</code> is <code>null</code>
187     * @throws DimensionMismatchException
188     *             if the length of the <code>categoryData</code> array is less
189     *             than 2 or a contained <code>double[]</code> array does not
190     *             contain at least two values
191     */
192    private AnovaStats anovaStats(final Collection<double[]> categoryData)
193        throws NullArgumentException, DimensionMismatchException {
194
195        MathUtils.checkNotNull(categoryData);
196
197        final Collection<SummaryStatistics> categoryDataSummaryStatistics =
198                new ArrayList<SummaryStatistics>(categoryData.size());
199
200        // convert arrays to SummaryStatistics
201        for (final double[] data : categoryData) {
202            final SummaryStatistics dataSummaryStatistics = new SummaryStatistics();
203            categoryDataSummaryStatistics.add(dataSummaryStatistics);
204            for (final double val : data) {
205                dataSummaryStatistics.addValue(val);
206            }
207        }
208
209        return anovaStats(categoryDataSummaryStatistics, false);
210
211    }
212
213    /**
214     * Performs an ANOVA test, evaluating the null hypothesis that there
215     * is no difference among the means of the data categories.
216     *
217     * <p><strong>Preconditions</strong>: <ul>
218     * <li>The categoryData <code>Collection</code> must contain
219     * <code>double[]</code> arrays.</li>
220     * <li> There must be at least two <code>double[]</code> arrays in the
221     * <code>categoryData</code> collection and each of these arrays must
222     * contain at least two values.</li>
223     * <li>alpha must be strictly greater than 0 and less than or equal to 0.5.
224     * </li></ul></p><p>
225     * This implementation uses the
226     * {@link org.apache.commons.math3.distribution.FDistribution
227     * commons-math F Distribution implementation} to estimate the exact
228     * p-value, using the formula<pre>
229     *   p = 1 - cumulativeProbability(F)</pre>
230     * where <code>F</code> is the F value and <code>cumulativeProbability</code>
231     * is the commons-math implementation of the F distribution.</p>
232     * <p>True is returned iff the estimated p-value is less than alpha.</p>
233     *
234     * @param categoryData <code>Collection</code> of <code>double[]</code>
235     * arrays each containing data for one category
236     * @param alpha significance level of the test
237     * @return true if the null hypothesis can be rejected with
238     * confidence 1 - alpha
239     * @throws NullArgumentException if <code>categoryData</code> is <code>null</code>
240     * @throws DimensionMismatchException if the length of the <code>categoryData</code>
241     * array is less than 2 or a contained <code>double[]</code> array does not have
242     * at least two values
243     * @throws OutOfRangeException if <code>alpha</code> is not in the range (0, 0.5]
244     * @throws ConvergenceException if the p-value can not be computed due to a convergence error
245     * @throws MaxCountExceededException if the maximum number of iterations is exceeded
246     */
247    public boolean anovaTest(final Collection<double[]> categoryData,
248                             final double alpha)
249        throws NullArgumentException, DimensionMismatchException,
250        OutOfRangeException, ConvergenceException, MaxCountExceededException {
251
252        if ((alpha <= 0) || (alpha > 0.5)) {
253            throw new OutOfRangeException(
254                    LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL,
255                    alpha, 0, 0.5);
256        }
257        return anovaPValue(categoryData) < alpha;
258
259    }
260
261    /**
262     * This method actually does the calculations (except P-value).
263     *
264     * @param categoryData <code>Collection</code> of <code>double[]</code>
265     * arrays each containing data for one category
266     * @param allowOneElementData if true, allow computation for one catagory
267     * only or for one data element per category
268     * @return computed AnovaStats
269     * @throws NullArgumentException if <code>categoryData</code> is <code>null</code>
270     * @throws DimensionMismatchException if <code>allowOneElementData</code> is false and the number of
271     * categories is less than 2 or a contained SummaryStatistics does not contain
272     * at least two values
273     */
274    private AnovaStats anovaStats(final Collection<SummaryStatistics> categoryData,
275                                  final boolean allowOneElementData)
276        throws NullArgumentException, DimensionMismatchException {
277
278        MathUtils.checkNotNull(categoryData);
279
280        if (!allowOneElementData) {
281            // check if we have enough categories
282            if (categoryData.size() < 2) {
283                throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_CATEGORIES_REQUIRED,
284                                                     categoryData.size(), 2);
285            }
286
287            // check if each category has enough data
288            for (final SummaryStatistics array : categoryData) {
289                if (array.getN() <= 1) {
290                    throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_VALUES_IN_CATEGORY_REQUIRED,
291                                                         (int) array.getN(), 2);
292                }
293            }
294        }
295
296        int dfwg = 0;
297        double sswg = 0;
298        double totsum = 0;
299        double totsumsq = 0;
300        int totnum = 0;
301
302        for (final SummaryStatistics data : categoryData) {
303
304            final double sum = data.getSum();
305            final double sumsq = data.getSumsq();
306            final int num = (int) data.getN();
307            totnum += num;
308            totsum += sum;
309            totsumsq += sumsq;
310
311            dfwg += num - 1;
312            final double ss = sumsq - ((sum * sum) / num);
313            sswg += ss;
314        }
315
316        final double sst = totsumsq - ((totsum * totsum) / totnum);
317        final double ssbg = sst - sswg;
318        final int dfbg = categoryData.size() - 1;
319        final double msbg = ssbg / dfbg;
320        final double mswg = sswg / dfwg;
321        final double F = msbg / mswg;
322
323        return new AnovaStats(dfbg, dfwg, F);
324
325    }
326
327    /**
328        Convenience class to pass dfbg,dfwg,F values around within OneWayAnova.
329        No get/set methods provided.
330    */
331    private static class AnovaStats {
332
333        /** Degrees of freedom in numerator (between groups). */
334        private final int dfbg;
335
336        /** Degrees of freedom in denominator (within groups). */
337        private final int dfwg;
338
339        /** Statistic. */
340        private final double F;
341
342        /**
343         * Constructor
344         * @param dfbg degrees of freedom in numerator (between groups)
345         * @param dfwg degrees of freedom in denominator (within groups)
346         * @param F statistic
347         */
348        private AnovaStats(int dfbg, int dfwg, double F) {
349            this.dfbg = dfbg;
350            this.dfwg = dfwg;
351            this.F = F;
352        }
353    }
354
355}